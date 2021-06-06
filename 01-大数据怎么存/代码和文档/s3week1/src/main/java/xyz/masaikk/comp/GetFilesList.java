package xyz.masaikk.comp;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class GetFilesList {
    public static List<File> getFileList(String strPath) {
        List<File> filelist = new ArrayList<>();
        File dir = new File(strPath);
        File[] files = dir.listFiles(); // 该文件目录下文件全部放入数组
        if (files != null) {
            for (File file : files) {
                String fileName = file.getName();
                if (file.isDirectory()) {
                    getFileList(file.getAbsolutePath()); //遍历子文件夹里面的东西
                } else if (fileName.endsWith("exe")) { // 以exe结尾的文件
                    String strFileName = file.getAbsolutePath();
                    filelist.add(file);
                } else {
                    filelist.add(file);
                    continue;
                }
            }
        }
        return filelist;
    }

}
