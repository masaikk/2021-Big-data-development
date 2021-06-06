package xyz.masaikk.comp;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class WatchFileTest {

    @Test
    public void startWatch() throws IOException, InterruptedException {

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
        WatchFile.startWatch(s3,fileFolderPath);
    }
}