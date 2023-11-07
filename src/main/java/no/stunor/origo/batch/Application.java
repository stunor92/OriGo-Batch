package no.stunor.origo.batch;

import lombok.extern.slf4j.Slf4j;
import no.stunor.origo.batch.converter.OrganisationConverter;
import no.stunor.origo.batch.converter.RegionConverter;
import no.stunor.origo.batch.model.Eventor;
import no.stunor.origo.batch.model.Organisation;
import no.stunor.origo.batch.model.Region;
import no.stunor.origo.batch.services.EventorApiException;
import no.stunor.origo.batch.services.EventorService;
import no.stunor.origo.batch.services.FirestoreService;

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
    public CommandLineRunner start(FirestoreService firestoreService, EventorService eventorService){
        return args -> {
            log.info("Start batch job...");
        
            List<Eventor> eventorList = firestoreService.getEventorList();



            
            for(Eventor eventor :eventorList){
                log.info("Updating organisations in {}.", eventor.getName());

                List<Organisation> existingOrganisations = firestoreService.getOrganisationList(eventor);
                List<Region> existingRegions = firestoreService.getRegionList(eventor);

                try {
                    List<org.iof.eventor.Organisation> eventorOrganisations = eventorService.getOrganisations(eventor.getBaseUrl(), eventor.getApiKey()).getOrganisation();
                    log.info("Found {} organisations in {}.", eventorOrganisations.size(), eventor.getName());

                    log.info("Start update regions in {}.", eventor.getName());
                    List<Region> regions = RegionConverter.convertRegions(eventorOrganisations);

                    for(Region region: regions){
                        firestoreService.updateRegion(region, eventor);
                    }
                    log.info("Finished update of {} regions in {}.", regions.size(), eventor.getName());
                    for(Region r : existingRegions){
                        boolean found = false;
                        for(Region r1 : regions){
                            if(r.getId().equals(r1.getId())){
                                found = true;
                            }
                        }
                        if(!found){
                            firestoreService.deleteRegion(r, eventor);
                            log.info("Deleted region {} in {}.", r.getName(), eventor.getName());
                        }
                    }

                    log.info("Start update organisations in {}.", eventor.getName());
                    List<Organisation> organisations = OrganisationConverter.convertOrganisations(eventorOrganisations, eventor, regions);
                    for(Organisation organisation : organisations){
                        firestoreService.updateOrganisation(organisation, eventor);
                    }
                    log.info("Finish to update organisations in {}.", eventor.getName());

                    for(Organisation o :existingOrganisations){
                        boolean found = false;
                        for(Organisation o1 : organisations){
                            if(o.getId().equals(o1.getId())){
                                found = true;
                            }
                        }
                        if(!found){
                            firestoreService.deleteOrganisation(o, eventor);
                            log.info("Deleted organisation {} in {}.", o.getName(), eventor.getName());
                        }
                    }

                } catch (EventorApiException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


                /*try {

                 

                    log.info("Start update organisations in {}.", eventor.getName());
                    List<Organisation> organisations = OrganisationConverter.convertOrganisations(eventorOrganisations, eventor, regions);
                    for(Organisation o : organisations){
                        for(Organisation o1 : existingOrganisations){
                            if(o.getId().equals(o1.getId())){
                                o.setId(o1.getId());
                                break;
                            }
                        }
                        dynamoDBService.updateOrganisation(o);
                    }
                    log.info("Finish to update organisations in {}.", eventor.getName());
                } catch (EventorApiException e) {
                    e.printStackTrace();
                }*/
            }
        };
    }

}