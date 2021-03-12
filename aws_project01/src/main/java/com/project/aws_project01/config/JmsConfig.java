package com.project.aws_project01.config;

import javax.jms.Session;

import com.amazon.sqs.javamessaging.ProviderConfiguration;
import com.amazon.sqs.javamessaging.SQSConnectionFactory;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.support.destination.DynamicDestinationResolver;

@Configuration
@EnableJms
public class JmsConfig {

    @Value("${spring.profiles.active}")
    private String springProfilesActive;

    @Value("${aws.endpoint}")
    private String awsEndpoint;

    @Value("${aws.region}")
    private String awsRegion;

    private SQSConnectionFactory sqsConnectionFactory;

    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory() {
        if (springProfilesActive.equals("dev")) {
            sqsConnectionFactory = new SQSConnectionFactory(new ProviderConfiguration(),
                    AmazonSQSClientBuilder.standard()
                            .withEndpointConfiguration(
                                    new AwsClientBuilder.EndpointConfiguration(awsEndpoint, awsRegion))
                            .withCredentials(new DefaultAWSCredentialsProviderChain()).build());
        } else {
            sqsConnectionFactory = new SQSConnectionFactory(new ProviderConfiguration(),
                    AmazonSQSClientBuilder.standard().withRegion(awsRegion)
                            .withCredentials(new DefaultAWSCredentialsProviderChain()).build());
        }

        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(sqsConnectionFactory);
        factory.setDestinationResolver(new DynamicDestinationResolver());
        factory.setConcurrency("2");
        factory.setSessionAcknowledgeMode(Session.CLIENT_ACKNOWLEDGE);

        return factory;
    }
}
