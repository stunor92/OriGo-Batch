package no.stunor.origo.batch.model.dynamoDB;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "${origo.config.tables.eventor}")
public class Eventor {
    private String id;
    private String name;
    private String baseUrl;
    private String apiKey;

    @DynamoDBHashKey
    public String getId() {
        return id;
    }

    @DynamoDBAttribute
    public String getName() {
        return name;
    }
    
    @DynamoDBAttribute
    public String getBaseUrl() {
        return baseUrl;
    }

    @DynamoDBAttribute
    public String getApiKey() {
        return apiKey;
    }
}
