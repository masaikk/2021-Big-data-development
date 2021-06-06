package xyz.masaikk.comp;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;

public class MultipartFileHandler {
/*    private final static String bucketName = "hujianqiao02";
    private final static String filePath = "C:\\Users\\masaikk\\Desktop\\big_file";
    private final static String accessKey = "29E2BC5B6851CD32568A";
    private final static String secretKey =
            "WzVDQTFCMjlBMjU2NzQ4MTlFMTU3MjdDMzMyQTg1";
    private final static String serviceEndpoint =
            "http://scut.depts.bingosoft.net:29997";
    private static long partSize = 3 << 20;
    private final static String signingRegion = "";*/

    /*public static void main(String[] args) {
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

        String keyName = Paths.get(filePath).getFileName().toString();

        // Create a list of UploadPartResponse objects. You get one of these
        // for each part upload.
        ArrayList<PartETag> partETags = new ArrayList<PartETag>();
        File file = new File(filePath);
        long contentLength = file.length();
        String uploadId = null;

        try {
            // Step 1: Initialize.
            InitiateMultipartUploadRequest initRequest =
                    new InitiateMultipartUploadRequest(bucketName, keyName);
            uploadId = s3.initiateMultipartUpload(initRequest).getUploadId();
            System.out.format("Created upload ID was %s\n", uploadId);

            // Step 2: Upload parts.
            long filePosition = 0;
            for (int i = 1; filePosition < contentLength; i++) {
                // Last part can be less than 5 MB. Adjust part size.
                partSize = Math.min(partSize, contentLength - filePosition);

                // Create request to upload a part.
                UploadPartRequest uploadRequest = new UploadPartRequest()
                        .withBucketName(bucketName)
                        .withKey(keyName)
                        .withUploadId(uploadId)
                        .withPartNumber(i)
                        .withFileOffset(filePosition)
                        .withFile(file)
                        .withPartSize(partSize);

                // Upload part and add response to our list.
                System.out.format("Uploading part %d\n", i);
                partETags.add(s3.uploadPart(uploadRequest).getPartETag());
//                System.out.println(s3.uploadPart(uploadRequest).getPartETag());

                filePosition += partSize;
            }

            // Step 3: Complete.
            System.out.println("Completing upload");
            CompleteMultipartUploadRequest compRequest =
                    new CompleteMultipartUploadRequest(bucketName, keyName, uploadId, partETags);

            s3.completeMultipartUpload(compRequest);
        } catch (Exception e) {
            System.err.println(e.toString());
            if (uploadId != null && !uploadId.isEmpty()) {
                // Cancel when error occurred
                System.out.println("Aborting upload");
                s3.abortMultipartUpload(new AbortMultipartUploadRequest(bucketName, keyName, uploadId));
            }
            System.exit(1);
        }
        System.out.println("Done!");


    }*/

    public static void firstUpload(AmazonS3 s3, Path fileFolderPath) throws IOException {
        final String bucketName = GetProperties.getValueByKey("config.properties", "bucketName");
        GetProperties.writeProperties("config.properties", "multipartFileStatus", "1");
        String keyName = Paths.get(String.valueOf(fileFolderPath)).getFileName().toString();
        InitiateMultipartUploadRequest initRequest =
                new InitiateMultipartUploadRequest(bucketName, keyName);
        String uploadId = s3.initiateMultipartUpload(initRequest).getUploadId();
        System.out.format("Created upload ID was %s\n", uploadId);
        File file = new File(String.valueOf(fileFolderPath));
        ArrayList<PartETag> partETags = new ArrayList<PartETag>();

        final long contentLength = file.length();
        // Step 2: Upload parts.
        long filePosition = 0;
        GetProperties.writeProperties("config.properties", "multipartFileID", uploadId);

        long partSize = Integer.parseInt(Objects.requireNonNull(GetProperties.getValueByKey("config.properties", "multipartFilePartLength"))) << 20;
        for (int i = 1; filePosition < contentLength; i++) {
            // Last part can be less than 5 MB. Adjust part size.
            partSize = Math.min(partSize, contentLength - filePosition);

            // Create request to upload a part.
            UploadPartRequest uploadRequest = new UploadPartRequest()
                    .withBucketName(bucketName)
                    .withKey(keyName)
                    .withUploadId(uploadId)
                    .withPartNumber(i)
                    .withFileOffset(filePosition)
                    .withFile(file)
                    .withPartSize(partSize);
            // Upload part and add response to our list.
            System.out.format("Uploading part %d\n", i);
            PartETag tempPartETag=s3.uploadPart(uploadRequest).getPartETag();
            partETags.add(tempPartETag);
            System.out.println(tempPartETag.getETag());
            //record partETag in properties
            GetProperties.writeProperties("config.properties","partETag"+Integer.toString(i),tempPartETag.getETag());

            filePosition += partSize;

        }

        System.out.println("Completing upload");
        CompleteMultipartUploadRequest compRequest =
                new CompleteMultipartUploadRequest(bucketName, keyName, uploadId, partETags);

        s3.completeMultipartUpload(compRequest);
        GetProperties.writeProperties("config.properties", "multipartFileStatus", "0");
        GetProperties.removeAllPartETagsFromProp("config.properties");
    }

    public static void firstDownload(AmazonS3 s3,String savePath,String keyName) throws IOException {
        final String bucketName= GetProperties.getValueByKey("config.properties", "bucketName");
        GetProperties.writeProperties("config.properties", "multipartFileStatus", "2");
        final String filePath=Paths.get(savePath,keyName).toString();
        long partSize = Integer.parseInt(Objects.requireNonNull(GetProperties.getValueByKey("config.properties", "multipartFilePartLength"))) << 20;
        File downloadFile=new File(filePath);
        S3Object o=null;
        S3ObjectInputStream s3is=null;
        FileOutputStream fos=null;

        // Step 1: Initialize.
        ObjectMetadata oMetaData = s3.getObjectMetadata(bucketName, keyName);
        final long contentLength = oMetaData.getContentLength();
        final GetObjectRequest downloadRequest =
                new GetObjectRequest(bucketName, keyName);

        fos = new FileOutputStream(downloadFile);

        // Step 2: Download parts.
        long filePosition = 0;
        for (int i = 1; filePosition < contentLength; i++) {
            // Last part can be less than 5 MB. Adjust part size.
            partSize = Math.min(partSize, contentLength - filePosition);

            // Create request to download a part.
            downloadRequest.setRange(filePosition, filePosition + partSize);
            o = s3.getObject(downloadRequest);

            // download part and save to local file.
            System.out.format("Downloading part %d\n", i);

            filePosition += partSize+1;
            s3is = o.getObjectContent();
            byte[] read_buf = new byte[64 * 1024];
            int read_len = 0;
            while ((read_len = s3is.read(read_buf)) > 0) {
                fos.write(read_buf, 0, read_len);
            }
        }

        // Step 3: Complete.
        System.out.println("Completing download");

        System.out.format("save %s to %s\n", keyName, filePath);
        GetProperties.writeProperties("config.properties", "multipartFileStatus", "0");


    }

    public static void continuteUploadMultipartFile(AmazonS3 s3, Path filePath) throws IOException {
        final String bucketName = GetProperties.getValueByKey("config.properties", "bucketName");
        final String uploadId=GetProperties.getValueByKey("config.properties", "multipartFileID");
        long partSize = Integer.parseInt(Objects.requireNonNull(GetProperties.getValueByKey("config.properties", "multipartFilePartLength"))) << 20;
        final String keyName = Paths.get(String.valueOf(filePath)).getFileName().toString();
        //continue to upload multipart file by upload ID.
        System.out.println("Loading upload data...");
        ArrayList<PartETag> eTageList = GetProperties.getPartETagsByProp("config.properties");

        long filePosition = eTageList.size()*partSize;
        File file = new File(String.valueOf(filePath));
        final long contentLength = file.length();
//        ArrayList<String> partUploadingETags = new ArrayList<String>(eTageList);
        ArrayList<PartETag> partUploadingETags=new ArrayList<PartETag>();
        partUploadingETags.addAll(eTageList);


        for (int i = eTageList.size()+1; filePosition < contentLength; i++) {
            // Last part can be less than 5 MB. Adjust part size.
            partSize = Math.min(partSize, contentLength - filePosition);

            // Create request to upload a part.
            UploadPartRequest uploadRequest = new UploadPartRequest()
                    .withBucketName(bucketName)
                    .withKey(keyName)
                    .withUploadId(uploadId)
                    .withPartNumber(i)
                    .withFileOffset(filePosition)
                    .withFile(file)
                    .withPartSize(partSize);
            // Upload part and add response to our list.
            System.out.format("Uploading part %d\n", i);
            PartETag tempPartETag=s3.uploadPart(uploadRequest).getPartETag();
            partUploadingETags.add(tempPartETag);
            System.out.println(tempPartETag.getETag());
            //record partETag in properties
            GetProperties.writeProperties("config.properties","partETag"+Integer.toString(i),tempPartETag.getETag());

            filePosition += partSize;

        }

        System.out.println("Completing upload");
        CompleteMultipartUploadRequest compRequest =
                new CompleteMultipartUploadRequest(bucketName, keyName, uploadId, partUploadingETags);

        s3.completeMultipartUpload(compRequest);
        GetProperties.writeProperties("config.properties", "multipartFileStatus", "0");

        GetProperties.removeAllPartETagsFromProp("config.properties");



    }
}
