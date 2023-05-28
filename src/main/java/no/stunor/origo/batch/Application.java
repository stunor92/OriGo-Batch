package no.stunor.origo.batch;

import lombok.extern.slf4j.Slf4j;
import no.stunor.origo.batch.converter.OrganisationConverter;
import no.stunor.origo.batch.model.dynamoDb.Eventor;
import no.stunor.origo.batch.model.dynamoDb.Organisation;
import no.stunor.origo.batch.model.eventor.EventorOrganisation;
import no.stunor.origo.batch.services.DynamoDbService;
import no.stunor.origo.batch.services.EventorApiException;
import no.stunor.origo.batch.services.EventorService;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@Slf4j
@SpringBootApplication
public class Application {

    public static void main(String[] args){
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner start(DynamoDbService dynamoDBService, EventorService eventorService){
        return args -> {
            log.info("Start batch job...");
        
            List<Eventor> eventorList = dynamoDBService.getEventorList();

            List<Organisation> existingOrganisations = dynamoDBService.getOrganisationList();

            for(Eventor eventor :eventorList){
                log.info("Start update organisations in {}.", eventor.getName());
                try {
                    List<EventorOrganisation> eventorOrganisations = eventorService.getOrganisations(eventor.getBaseUrl(), eventor.getApiKey()).getOrganisation();
                    log.info("Found {} organisations in {}.", eventorOrganisations.size(), eventor.getName());
                    List<Organisation> organisations = OrganisationConverter.convertOrganisations(eventorOrganisations, eventor);
                    for(Organisation o : organisations){
                        for(Organisation o1 : existingOrganisations){
                            if(o.getOrganisationId().equals(o1.getOrganisationId()) && o.getEventorId().equals(o1.getEventorId())){
                                o.setId(o1.getId());
                                o.setCreatedAt(o1.getCreatedAt());
                                o.setVersion(o1.getVersion()+1);
                                break;
                            }
                        }
                        dynamoDBService.updateOrganisation(o);
                    }
                    log.info("Finish to update organisations in {}.", eventor.getName());
                } catch (EventorApiException e) {
                    e.printStackTrace();
                }
            }
        };
    }

}