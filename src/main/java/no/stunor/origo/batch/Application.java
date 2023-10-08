package no.stunor.origo.batch;

import lombok.extern.slf4j.Slf4j;
import no.stunor.origo.batch.converter.OrganisationConverter;
import no.stunor.origo.batch.converter.RegionConverter;
import no.stunor.origo.batch.model.Eventor;
import no.stunor.origo.batch.model.Organisation;
import no.stunor.origo.batch.model.Region;
import no.stunor.origo.batch.services.DynamoDbService;
import no.stunor.origo.batch.services.EventorApiException;
import no.stunor.origo.batch.services.EventorService;

import java.util.List;

import org.joda.time.Instant;
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
            List<Region> existingRegions = dynamoDBService.getRegionList();

            
            for(Eventor eventor :eventorList){
                try {
                    List<org.iof.eventor.Organisation> eventorOrganisations = eventorService.getOrganisations(eventor.getBaseUrl(), eventor.getApiKey()).getOrganisation();
                    log.info("Found {} organisations in {}.", eventorOrganisations.size(), eventor.getName());

                    log.info("Start update regions in {}.", eventor.getName());
                    List<Region> regions = RegionConverter.convertRegions(eventorOrganisations, eventor);

                    for(Region region: regions){
                        for(Region r : existingRegions){
                            if(region.getEventorId().equals(r.getEventorId()) && region.getOrganisationNumber().equals(r.getOrganisationNumber())){
                                region.setId(r.getId());
                                region.setCreatedAt(r.getCreatedAt());
                                region.setVersion(r.getVersion()+1);
                                break;
                            }
                        }
                        dynamoDBService.updateRegion(region);
                    }
                    log.info("Finished update of {} regions in {}.", regions.size(), eventor.getName());

                    for(Region r : existingRegions){
                        if(r.getEventorId().equals(eventor.getId())){
                            boolean found = false;
                            for(Region r1 : regions){
                                if(r.getId().equals(r1.getId())){
                                    found = true;
                                }
                            }
                            if(!found){
                                r.setDeleted(true);
                                r.setUpdatedAt(Instant.now().toString());
                                r.setLastChangedAt(Instant.now().getMillis());
                                r.setVersion(r.getVersion()+1);
                                dynamoDBService.updateRegion(r);
                            }
                        }
                    }


                    log.info("Start update organisations in {}.", eventor.getName());
                    List<Organisation> organisations = OrganisationConverter.convertOrganisations(eventorOrganisations, eventor, regions);
                    for(Organisation o : organisations){
                        for(Organisation o1 : existingOrganisations){
                            if(o.getOrganisationNumber().equals(o1.getOrganisationNumber()) && o.getEventorId().equals(o1.getEventorId())){
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