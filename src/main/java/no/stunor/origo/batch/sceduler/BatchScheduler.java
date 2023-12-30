package no.stunor.origo.batch.sceduler;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.google.cloud.Timestamp;

import lombok.extern.slf4j.Slf4j;
import no.stunor.origo.batch.model.Eventor;
import no.stunor.origo.batch.model.Region;
import no.stunor.origo.batch.services.EventorApiException;
import no.stunor.origo.batch.services.EventorService;
import no.stunor.origo.batch.services.FirestoreService;
import no.stunor.origo.batch.services.OrganisationService;
import no.stunor.origo.batch.services.RegionService;

@Slf4j
@Component
@EnableScheduling
public record BatchScheduler(
    FirestoreService firestoreService, 
    EventorService eventorService, 
    RegionService regionService, 
    OrganisationService organisationService) {    


    @Scheduled(cron="0 9 0 * * *")
    public void sceduleJob() throws InterruptedException, ExecutionException, EventorApiException{
        log.info("Start batch job...");
        Timestamp startTtme = Timestamp.now();

    
        List<Eventor> eventorList = firestoreService.getEventorList();

        for(Eventor eventor : eventorList){
            log.info("Updating {}.", eventor.getName());

            List<org.iof.eventor.Organisation> eventorOrganisations = eventorService.getOrganisations(eventor.getBaseUrl(), eventor.getApiKey()).getOrganisation();
            log.info("Found {} organisations in {}.", eventorOrganisations.size(), eventor.getName());

            List<Region>  regions = regionService.updateRegions(eventor, eventorOrganisations);

            organisationService.updateOerganisations(eventor, eventorOrganisations, regions);
        }
        log.info("Start deleting deleted organisations...");

        if(startTtme != null){
            firestoreService.deleteNotUpdatedOrganisation(startTtme);
            firestoreService.deleteNotUpdatedRegions(startTtme);
        }
        log.info("Finished deleting deleted organisations.");
    }
}
