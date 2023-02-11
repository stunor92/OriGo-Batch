package no.stunor.origo.batch;

import lombok.extern.slf4j.Slf4j;
import no.stunor.origo.batch.model.DynamoDB.Eventor;
import no.stunor.origo.batch.services.DynamoDBService;

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
    public CommandLineRunner start(DynamoDBService dynamoDBService){
        return args -> {
            log.info("Start batch job...");
        
            List<Eventor> eventorList = dynamoDBService.getEventorList();

            for(Eventor eventor :eventorList){
                log.info("Start update organisations in {}.", eventor.getName());

            }
        };
    }

}