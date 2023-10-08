package no.stunor.origo.batch.configuration;

import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMappingException;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@Configuration
@EnableDynamoDBRepositories(basePackages = "no.stunor.origo.batch.repository")
public class AwsConfiguration {
    
    @Value("${dynamoDB.tables.suffix}")
    private String tableSuffix;


    @Bean
    public AmazonDynamoDB amazonDynamoDB(){
        return AmazonDynamoDBClientBuilder 
            .standard()
            .withCredentials(new DefaultAWSCredentialsProviderChain())
            .withRegion(Regions.EU_NORTH_1)
            .build();
    }

    @Bean
    public DynamoDBMapperConfig dynamoDBMapperConfig() {
        DynamoDBMapperConfig.Builder builder = new DynamoDBMapperConfig.Builder();
        builder.withTableNameResolver(tableNameResolver());
        return builder.build();
    }

    private DynamoDBMapperConfig.TableNameResolver tableNameResolver() {
        return (clazz, config) -> {
        final DynamoDBTable dynamoDBTable = clazz.getDeclaredAnnotation(DynamoDBTable.class);
        if (dynamoDBTable == null) {
            throw new DynamoDBMappingException(clazz + " not annotated with @DynamoDBTable");
        }
        return dynamoDBTable.tableName() + tableSuffix;
        };
    }
    
}
