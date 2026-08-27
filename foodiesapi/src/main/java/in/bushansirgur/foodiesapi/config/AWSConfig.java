package in.bushansirgur.foodiesapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListBucketsResponse;


@Configuration
public class AWSConfig {
    @Value("${aws.access.key}")
    private String accessKey;
    @Value("${aws.secret.key}")
    private String secretKey;
    @Value("${aws.region}")
    private String region;

    @Bean
    public S3Client s3Client() {
        // Masked logging (or removed) to avoid credential leak
        System.out.println("AWS Region initialized: " + region);
        S3Client s3 = S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey)))
                .build();

        try {
            // List the buckets to verify access
            ListBucketsResponse buckets = s3.listBuckets();
            System.out.println("S3 Buckets:");
            buckets.buckets().forEach(bucket -> System.out.println(bucket.name()));
            System.out.println("✅ S3 access confirmed!");
        } catch (Exception e) {
            System.err.println("❌ Failed to access S3: " + e.getMessage());
            e.printStackTrace();
        }

        return s3;
    }
}
