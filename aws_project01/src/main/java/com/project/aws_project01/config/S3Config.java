package com.project.aws_project01.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3Config {

    @Value("${spring.profiles.active}")
    private String springProfilesActive;

    @Value("${aws.endpoint}")
    private String awsEndpoint;

    @Value("${aws.region}")
    private String awsRegion;

    @Value("${aws.access.key.id}")
    private String awsAccessKeyId;

    @Value("${aws.secret.access.key}")
    private String awsSecretAccessKey;

    @Bean
    public AmazonS3 amazonS3Client() {
        if (springProfilesActive.equals("dev")) {
            return AmazonS3ClientBuilder.standard()
                    .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(awsEndpoint, awsRegion))
                    .withCredentials(new AWSStaticCredentialsProvider(
                            new BasicAWSCredentials(awsAccessKeyId, awsSecretAccessKey)))
                    .build();
        }

        return AmazonS3ClientBuilder.standard().withRegion(awsRegion)
                .withCredentials(new DefaultAWSCredentialsProviderChain()).build();
    }
}
