package xyz.masaikk.comp;

import com.amazonaws.services.s3.AmazonS3;

import java.io.*;
import java.nio.file.*;

import java.util.LinkedList;
import java.util.Objects;

public class WatchFile {
    public static void startWatch(AmazonS3 s3, String fileFolderPath) throws IOException, InterruptedException {
        long partSize = Integer.parseInt(Objects.requireNonNull(GetProperties.getValueByKey("config.properties", "multipartFilePartLength"))) << 20;
        String fileRootPath = (GetProperties.getValueByKey("config.properties", "fileFolderPath"));
        // 获取文件系统的WatchService对象
        System.out.println("Listening " + fileRootPath);

        WatchService watchService = FileSystems.getDefault().newWatchService();
        assert fileRootPath != null;
        Paths.get(fileRootPath).register(watchService
                , StandardWatchEventKinds.ENTRY_CREATE
                , StandardWatchEventKinds.ENTRY_MODIFY
                , StandardWatchEventKinds.ENTRY_DELETE);

        File file = new File(fileRootPath);
        LinkedList<File> fList = new LinkedList<File>();
        fList.addLast(file);
        while (fList.size() > 0) {
            File f = fList.removeFirst();
            if (f.listFiles() == null)
                continue;
            for (File file2 : Objects.requireNonNull(f.listFiles())) {
                if (file2.isDirectory()) {//下一级目录
                    fList.addLast(file2);
                    //依次注册子目录
                    Paths.get(file2.getAbsolutePath()).register(watchService
                            , StandardWatchEventKinds.ENTRY_CREATE
                            , StandardWatchEventKinds.ENTRY_MODIFY
                            , StandardWatchEventKinds.ENTRY_DELETE);
                }
            }
        }

        while (true) {
            // 获取下一个文件改动事件
            WatchKey key = watchService.take();
            for (WatchEvent<?> event : key.pollEvents()) {
                System.out.println(event.context() + " --> " + event.kind());
                switch (event.kind().toString()) {
                    case "ENTRY_CREATE": {
//                        System.out.println("ENTRY_CREATE");
                        Path filePathToCreate = Paths.get(fileRootPath, String.valueOf(event.context()));

//                        System.out.println(new File(String.valueOf(filePathToCreate)).length());
                        //length of this file
                        if (new File(String.valueOf(filePathToCreate)).length() < partSize) {
                            SingleFileHandler.createFile(filePathToCreate, s3);
//                      if length is less than part size, just upload it.
                            break;
                        } else {
//                            multipart upload
                            MultipartFileHandler.firstUpload(s3, filePathToCreate);
                            break;
                        }

                    }
                    case "ENTRY_MODIFY": {
//                      System.out.println("ENTRY_MODIFY");
                        Path filePathToModify = Paths.get(fileRootPath, String.valueOf(event.context()));
//                        System.out.println(new File(String.valueOf(filePathToModify)).length());
                        //length of this file

                        if(new File(String.valueOf(filePathToModify)).length()<partSize){

                            SingleFileHandler.modifyFile(filePathToModify, s3);
                            break;
                        }else {
                            //multipart upload
//                            MultipartFileHandler.firstUpload(s3, filePathToModify);
                            break;
                        }


                    }
                    case "ENTRY_DELETE": {
                        Path filePathToDelete = Paths.get(fileRootPath, String.valueOf(event.context()));
                        SingleFileHandler.deleteFile(filePathToDelete, s3);
                        break;
                    }
                    default: {
                        break;
                    }
                }
            }
            // 重设WatchKey
            boolean valid = key.reset();
            // 如果重设失败，退出监听
            if (!valid) {
                break;
            }
            System.out.println("Listening " + fileRootPath);
        }
    }
}
