package no.stunor.origo.batch.services;


import lombok.extern.slf4j.Slf4j;
import no.stunor.origo.batch.model.dynamoDB.Eventor;

import java.util.List;

import org.springframework.stereotype.Component;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;

@Slf4j
@Component
public class DynamoDBService {

    public final AmazonDynamoDB amazonDynamoDB;

    public DynamoDBService(AmazonDynamoDB amazonDynamoDB){
        this.amazonDynamoDB = amazonDynamoDB;
    }

    public List<Eventor> getEventorList(){
        log.info("Start to fecth evnetorList.");
        DynamoDBMapper mapper = new DynamoDBMapper(amazonDynamoDB);
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();

        List<Eventor> eventorList;
        eventorList = mapper.scan(Eventor.class, scanExpression);
        log.info("Found {} eventor in DynamoDB.", eventorList.size());
        return eventorList;
    }
}
