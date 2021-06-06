package xyz.masaikk.main;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import xyz.masaikk.comp.CalMD5;
import xyz.masaikk.comp.GetProperties;
import xyz.masaikk.comp.MultipartFileHandler;
import xyz.masaikk.comp.WatchFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        initialize();
    }

    public static void initialize() throws IOException, InterruptedException {
        String bucketName = GetProperties.getValueByKey("config.properties", "bucketName");
        String accessKey = GetProperties.getValueByKey("config.properties", "accessKey");
        String secretKey = GetProperties.getValueByKey("config.properties", "secretKey");
        String serviceEndpoint = GetProperties.getValueByKey("config.properties", "serviceEndpoint");
        String signingRegion = GetProperties.getValueByKey("config.properties", "signingRegion");
        String fileFolderPath = GetProperties.getValueByKey("config.properties", "fileFolderPath");

        assert accessKey != null;
        assert secretKey != null;
        final BasicAWSCredentials credentials =
                new BasicAWSCredentials(accessKey, secretKey);
        final ClientConfiguration ccfg = new ClientConfiguration().
                withUseExpectContinue(true);

        final AwsClientBuilder.EndpointConfiguration endpoint =
                new AwsClientBuilder.EndpointConfiguration(serviceEndpoint, signingRegion);

        final AmazonS3 s3 = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withClientConfiguration(ccfg)
                .withEndpointConfiguration(endpoint)
                .withPathStyleAccessEnabled(true)
                .build();


/*        ListObjectsV2Result result = s3.listObjectsV2(bucketName);
        List<S3ObjectSummary> objects = result.getObjectSummaries();
        for (S3ObjectSummary os : objects) {
            System.out.println("* " + os.getKey());
        }*/
        uploadFilesFromPath(bucketName, fileFolderPath, s3);
        /*deleteFilesFromPath(bucketName, fileFolderPath, s3);
        renewFilesFromPath(bucketName, fileFolderPath, s3);*/
        updateFromS3(bucketName, fileFolderPath, s3);

        WatchFile.startWatch(s3, fileFolderPath);

    }

    public static void uploadFilesFromPath(String bucketName, String fileFolderPath, AmazonS3 s3) throws IOException {
        final long partSize = Integer.parseInt(Objects.requireNonNull(GetProperties.getValueByKey("config.properties", "multipartFilePartLength"))) << 20;
        int multipartFileStatus = Integer.parseInt(Objects.requireNonNull(GetProperties.getValueByKey("config.properties", "multipartFileStatus")));
        if (multipartFileStatus == 1) {
            //检查是否当前有未上传完全的大文件，如果有，则继续上传
            MultipartFileHandler.continuteUploadMultipartFile(s3, Paths.get(fileFolderPath, GetProperties.getValueByKey("config.properties", "multipartFileName")));
        }

        File file = new File(fileFolderPath);
        File[] fileNamesList = file.listFiles();
        assert fileNamesList != null;
/*        for (File f : fileNamesList) {
            System.out.println(Paths.get(String.valueOf(f)).getFileName().toString());
        }*/
        ListObjectsV2Result result = s3.listObjectsV2(bucketName);
        List<S3ObjectSummary> objects = result.getObjectSummaries();
        ArrayList<String> objNamesList = new ArrayList<String>();
        for (S3ObjectSummary objs : objects) {
            System.out.println("* " + objs.getKey() + " ETag: " + objs.getETag());
//            for (File f : fileNamesList) {
//                System.out.println(Paths.get(String.valueOf(f)).getFileName().toString());
            objNamesList.add(objs.getKey());
//            }
        }
        System.out.println("Objs name list:" + objNamesList.toString());
        for (File f : fileNamesList) {
//            System.out.println(Paths.get(String.valueOf(f)).getFileName().toString());
            if (!objNamesList.contains(Paths.get(String.valueOf(f)).getFileName().toString())) {
                if (f.length() < partSize) {
                    s3.putObject(bucketName, Paths.get(String.valueOf(f)).getFileName().toString(), new File(String.valueOf(f)));
                    System.out.println(Paths.get(String.valueOf(f)).getFileName().toString() + " Uploading...");
                } else {

                    multipartFileStatus = Integer.parseInt(Objects.requireNonNull(GetProperties.getValueByKey("config.properties", "multipartFileStatus")));

                    if (multipartFileStatus == 0) {
                        //upload big file by MultipartUpload
                        GetProperties.writeProperties("config.properties", "multipartFileName", Paths.get(String.valueOf(f)).getFileName().toString());
                        MultipartFileHandler.firstUpload(s3, f.toPath());
                    } else if (multipartFileStatus == 1) {
                        //continue upload this big file.
                        if (f.getName().equals(GetProperties.getValueByKey("config.properties", "multipartFileName"))) {
                            MultipartFileHandler.continuteUploadMultipartFile(s3, f.toPath());
                        }
                    } else {
                        break;
                    }


                }
            }
        }
    }

    public static void renewFilesFromPath(String bucketName, String filePath, AmazonS3 s3) {

    }

    public static void deleteFilesFromPath(String bucketName, String filePath, AmazonS3 s3) {
        File file = new File(Objects.requireNonNull(filePath));
        File[] fileNamesList = file.listFiles();
        assert fileNamesList != null;
        ListObjectsV2Result result = s3.listObjectsV2(bucketName);
        List<S3ObjectSummary> objects = result.getObjectSummaries();
        ArrayList<String> demsitFileNamesList = new ArrayList<String>();
        for (File f : fileNamesList) {
//            System.out.println(Paths.get(String.valueOf(f)).getFileName().toString());
            demsitFileNamesList.add(Paths.get(String.valueOf(f)).getFileName().toString());
        }
        for (S3ObjectSummary objs : objects) {
//            System.out.println("* " + objs.getKey()+" MD5: "+objs.getETag());
            if (!demsitFileNamesList.contains(objs.getKey())) {
                s3.deleteObject(bucketName, objs.getKey());
            }
        }
    }

    public static void updateFromS3(String bucketName, String fileFolderPath, AmazonS3 s3) throws IOException {
        long partSize = Integer.parseInt(Objects.requireNonNull(GetProperties.getValueByKey("config.properties", "multipartFilePartLength"))) << 20;
        File file = new File(Objects.requireNonNull(fileFolderPath));
        File[] fileNamesList = file.listFiles();
        assert fileNamesList != null;
        ListObjectsV2Result result = s3.listObjectsV2(bucketName);
        List<S3ObjectSummary> objects = result.getObjectSummaries();
        ArrayList<String> localFileNamesList = new ArrayList<String>();
        for (File f : fileNamesList) {
//            System.out.println(Paths.get(String.valueOf(f)).getFileName().toString());
            if (!f.isDirectory()) {
                localFileNamesList.add(Paths.get(String.valueOf(f)).getFileName().toString());
            }

        }
        String bigFileName = GetProperties.getValueByKey("config.properties", "multipartFileName");
        for (S3ObjectSummary objs : objects) {
//            System.out.println("* " + objs.getKey()+" MD5: "+objs.getETag());
            if (!localFileNamesList.contains(objs.getKey())) {
                if (objs.getSize() < partSize) {
                    S3Object o = s3.getObject(bucketName, objs.getKey());
                    S3ObjectInputStream s3is = o.getObjectContent();
                    System.out.println("Downloading " + objs.getKey());
                    FileOutputStream fos = new FileOutputStream(new File(String.valueOf(Paths.get(fileFolderPath, objs.getKey()))));
                    byte[] read_buf = new byte[1024];
                    int read_len = 0;
                    while ((read_len = s3is.read(read_buf)) > 0) {
                        fos.write(read_buf, 0, read_len);
                    }
                    s3is.close();
                    fos.close();
                } else {
                    MultipartFileHandler.firstDownload(s3, fileFolderPath, objs.getKey());
                }

            } else {

                if (!objs.getKey().equals(bigFileName)) {
                    if (!CalMD5.calculate(Paths.get(fileFolderPath, objs.getKey())).equals(objs.getETag())) {
                        File localFile = new File(String.valueOf(Paths.get(fileFolderPath, objs.getKey())));
                        if (localFile.lastModified() < objs.getLastModified().getTime()) {
                            S3Object o = s3.getObject(bucketName, objs.getKey());
                            S3ObjectInputStream s3is = o.getObjectContent();
                            System.out.println("Updating " + objs.getKey() + " from bucket");
                            FileOutputStream fos = new FileOutputStream(new File(String.valueOf(Paths.get(fileFolderPath, objs.getKey()))));
                            byte[] read_buf = new byte[1024];
                            int read_len = 0;
                            while ((read_len = s3is.read(read_buf)) > 0) {
                                fos.write(read_buf, 0, read_len);
                            }
                            s3is.close();
                            fos.close();
                        }
                    }
                }
            }
        }
        // TODO: add MD5 difference download handler
    }


}
