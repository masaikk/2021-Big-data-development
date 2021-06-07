package xyz.masaikk.comp;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MultipartFileHandlerTest {

    @Test
    public void firstUpload() throws IOException {
//        String bucketName = GetProperties.getValueByKey("config.properties", "bucketName");
        String accessKey = GetProperties.getValueByKey("config.properties", "accessKey");
        String secretKey = GetProperties.getValueByKey("config.properties", "secretKey");
        String serviceEndpoint = GetProperties.getValueByKey("config.properties", "serviceEndpoint");
        String signingRegion = "";
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

        assert fileFolderPath != null;
        Path bigFilePath = Paths.get(fileFolderPath,"big_file.txt");
        MultipartFileHandler.firstUpload(s3,bigFilePath);



    }

    @Test
    public void firstDownload() throws IOException{
        String accessKey = GetProperties.getValueByKey("config.properties", "accessKey");
        String secretKey = GetProperties.getValueByKey("config.properties", "secretKey");
        String serviceEndpoint = GetProperties.getValueByKey("config.properties", "serviceEndpoint");
        String signingRegion = GetProperties.getValueByKey("config.properties", "signingRegion");;
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

        MultipartFileHandler.firstDownload(s3,fileFolderPath,"big_file.txt");
    }

    @Test
    public void continueUpload() throws IOException {
        String accessKey = GetProperties.getValueByKey("config.properties", "accessKey");
        String secretKey = GetProperties.getValueByKey("config.properties", "secretKey");
        String serviceEndpoint = GetProperties.getValueByKey("config.properties", "serviceEndpoint");
        String signingRegion = GetProperties.getValueByKey("config.properties", "signingRegion");;
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
        Path bigFilePath = Paths.get(fileFolderPath,"big_file.txt");
        MultipartFileHandler.continueUploadMultipartFile(s3,bigFilePath);
    }
}