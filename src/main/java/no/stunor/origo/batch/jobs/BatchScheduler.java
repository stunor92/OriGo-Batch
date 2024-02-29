package no.stunor.origo.batch.jobs;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import no.stunor.origo.batch.api.EventorApiException;
import no.stunor.origo.batch.api.EventorService;
import no.stunor.origo.batch.data.EventorRepository;
import no.stunor.origo.batch.model.Eventor;
import no.stunor.origo.batch.model.Region;
import no.stunor.origo.batch.services.OrganisationService;
import no.stunor.origo.batch.services.RegionService;

@Slf4j
@Component
@EnableScheduling
public class BatchScheduler {
    @Autowired
    EventorRepository eventorRepository;
    @Autowired 
    EventorService eventorService;
    @Autowired
    RegionService regionService;
    @Autowired
    OrganisationService organisationService;   

    @Scheduled(cron="0 0 3 * * *")
    public void sceduleJob() throws InterruptedException, ExecutionException, EventorApiException{
        log.info("Start batch job...");

    
        List<Eventor> eventorList = eventorRepository.findAll().collectList().block();

        for(Eventor eventor : eventorList){
            log.info("Updating {}.", eventor.getName());

            List<org.iof.eventor.Organisation> eventorOrganisations = eventorService.getOrganisations(eventor.getBaseUrl(), eventor.getApiKey()).getOrganisation();
            log.info("Found {} organisations in {}.", eventorOrganisations.size(), eventor.getName());

            List<Region>  regions = regionService.updateRegions(eventor, eventorOrganisations);

            organisationService.updateOerganisations(eventor, eventorOrganisations, regions);
        }
       
    }
}
