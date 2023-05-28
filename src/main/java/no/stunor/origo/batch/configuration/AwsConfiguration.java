package no.stunor.origo.batch.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

@Configuration
public class AwsConfiguration {

    @Bean
    public DynamoDBMapper dynamoDBMapper() {
        return new DynamoDBMapper(getAmazonDynamoDB());
    }
    
    private AmazonDynamoDB getAmazonDynamoDB(){
        return AmazonDynamoDBClientBuilder.standard().withCredentials(new DefaultAWSCredentialsProviderChain()).build();
    }
    
}
