package no.stunor.origo.batch;

import lombok.extern.slf4j.Slf4j;
import no.stunor.origo.batch.model.dynamoDB.Eventor;
import no.stunor.origo.batch.model.eventor.Organisation;
import no.stunor.origo.batch.services.DynamoDBService;
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
    public CommandLineRunner start(DynamoDBService dynamoDBService, EventorService eventorService){
        return args -> {
            log.info("Start batch job...");
        
            List<Eventor> eventorList = dynamoDBService.getEventorList();

            for(Eventor eventor :eventorList){
                log.info("Start update organisations in {}.", eventor.getName());
                try {
                    List<Organisation> organisations = eventorService.getOrganisations(eventor.getBaseUrl(), eventor.getApiKey()).getOrganisation();
                    log.info("Found {} organisations in {}.", organisations.size(), eventor.getName());
                } catch (EventorApiException e) {
                    e.printStackTrace();
                }
            }
        };
    }

}