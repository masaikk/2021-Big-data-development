package xyz.masaikk.comp;

import com.amazonaws.services.s3.AmazonS3;

import java.io.File;
import java.nio.file.Path;

public class SingleFileHandler {
    public static String bucketName=GetProperties.getValueByKey("config.properties", "bucketName");
    public static void createFile(Path filePath, AmazonS3 s3){
        s3.putObject(bucketName, String.valueOf(filePath.getFileName()),new File(String.valueOf(filePath)));
        System.out.println("File "+filePath.getFileName()+" have been UPLOADED");
    }
    public static void modifyFile(Path filePath, AmazonS3 s3){
        s3.putObject(bucketName, String.valueOf(filePath.getFileName()),new File(String.valueOf(filePath)));
        System.out.println("File "+filePath.getFileName()+" have been MODIFIED");
    }
    public static void deleteFile(Path filePath,AmazonS3 s3){
        s3.deleteObject(bucketName,String.valueOf(filePath.getFileName()));
        System.out.println("File "+filePath.getFileName()+" have been DELETED");
    }


}
