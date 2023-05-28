package no.stunor.origo.batch.services;


import lombok.extern.slf4j.Slf4j;
import no.stunor.origo.batch.model.dynamoDb.Eventor;
import no.stunor.origo.batch.model.dynamoDb.Organisation;
import no.stunor.origo.batch.model.dynamoDb.Region;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;

@Slf4j
@Repository
public class DynamoDbService {

    @Value("${origo.config.tables.eventor}")
    String eventorTable;

    @Value("${origo.config.tables.organisation}")
    String organisationTable;

    @Autowired
    private DynamoDBMapper dynamoDBMapper;
    

    public List<Eventor> getEventorList(){
        log.info("Start to fecth evnetorList.");
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();

        List<Eventor> eventorList = dynamoDBMapper.scan(Eventor.class, scanExpression);
        log.info("Found {} eventor in DynamoDB.", eventorList.size());
        return eventorList;
    }

    public List<Region> getRegionList(){
        log.info("Start to fecth all regions.");
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();

        List<Region> regions = dynamoDBMapper.scan(Region.class, scanExpression);
        log.info("Found {} regions in DynamoDB.", regions.size());
        return regions;
    }

    public List<Organisation> getOrganisationList(){
        log.info("Start to fecth all organisatopns.");
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();

        List<Organisation> organisations = dynamoDBMapper.scan(Organisation.class, scanExpression);
        log.info("Found {} organisations in DynamoDB.", organisations.size());
        return organisations;
    }

    public void updateOrganisation(Organisation organisation){
        dynamoDBMapper.save(organisation);
    }

    public void updateRegion(Region region){
        dynamoDBMapper.save(region);
    }
}
