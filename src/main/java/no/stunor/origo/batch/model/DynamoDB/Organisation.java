package no.stunor.origo.batch.model.dynamoDb;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamoDBTable(tableName = "Organisation-igq2qtlqone6hp26nexxeqgp7e-staging")
public class Organisation {
 
    @DynamoDBHashKey
    @DynamoDBAutoGeneratedKey
    private String id;

    @DynamoDBAttribute
    private String organisationId;

    @DynamoDBAttribute
    private String name;

    @DynamoDBAttribute
    private String contactPerson;

    @DynamoDBAttribute
    private String email;

    @DynamoDBAttribute
    private String type;

    @DynamoDBAttribute
    private String eventorId;

    @DynamoDBAttribute
    private String parentOrganisation;
    
    @DynamoDBAttribute
    private String country;

    @DynamoDBAttribute
    private String createdAt;

    @DynamoDBAttribute
    private String updatedAt;

    @DynamoDBAttribute(attributeName = "_lastChangedAt")
    private Long lastChangedAt;

    @DynamoDBAttribute(attributeName = "_version")
    private int version;

    @DynamoDBAttribute(attributeName = "__typename")
    private String typename;
}