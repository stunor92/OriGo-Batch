package no.stunor.origo.batch.controller;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import no.stunor.origo.batch.api.EventorApiException;
import no.stunor.origo.batch.api.EventorService;
import no.stunor.origo.batch.data.EventorRepository;
import no.stunor.origo.batch.model.Eventor;
import no.stunor.origo.batch.model.Region;
import no.stunor.origo.batch.services.OrganisationService;
import no.stunor.origo.batch.services.RegionService;
import org.springframework.web.bind.annotation.PostMapping;


@Slf4j
@RestController
class BatchController {
    @Autowired
    EventorRepository eventorRepository;
    @Autowired 
    EventorService eventorService;
    @Autowired
    RegionService regionService;
    @Autowired
    OrganisationService organisationService;   

    @PostMapping("/updateOrganisations")
    public void uprdateOrigoOrganisations() {

        log.info("Start batch job...");
    
        List<Eventor> eventorList = eventorRepository.findAll().collectList().block();

        for(Eventor eventor : eventorList){
            try {
                log.info("Updating {}.", eventor.getName());

                List<org.iof.eventor.Organisation> eventorOrganisations = eventorService.getOrganisations(eventor.getBaseUrl(), eventor.getApiKey()).getOrganisation();
                log.info("Found {} organisations in {}.", eventorOrganisations.size(), eventor.getName());
    
                List<Region> regions;
                regions = regionService.updateRegions(eventor, eventorOrganisations);
                organisationService.updateOerganisations(eventor, eventorOrganisations, regions);

            } catch (InterruptedException | ExecutionException | EventorApiException e) {
                log.error("Error updating {}.", eventor.getName(), e);
            }

        }
       
    }
}
