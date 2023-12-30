package no.stunor.origo.batch.services;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.iof.eventor.Organisation;
import org.springframework.stereotype.Service;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentReference;

import lombok.extern.slf4j.Slf4j;
import no.stunor.origo.batch.model.Region;
    
@Slf4j
@Service
public record RegionService(FirestoreService firestoreService) {  

     public List<Region> updateRegions(DocumentReference eventorReference, List<Organisation>  organisations) throws InterruptedException, ExecutionException{
        log.info("Start update regions...");
        List<Region> regions = new ArrayList<>();
        for(Organisation organisation : organisations){
            if(!organisation.getOrganisationTypeId().getContent().equals("2")){
                continue;
            }
            Region region = createRegion(organisation, eventorReference);
            Region exisitingRegion = firestoreService.getRegion(eventorReference, region.getOrganisationNumber());
            if(exisitingRegion == null){
                firestoreService.createRegion(region);
            } else {
                region.setId(exisitingRegion.getId());
                firestoreService.updateRegion(region);
            }
            regions.add(region);
        }
        log.info("Finished update of {} regions.", regions.size());

        return regions;
    }

    public static Region createRegion(Organisation organisation, DocumentReference eventorReference){
        return new Region(
            null,
            organisation.getOrganisationId().getContent(),
            eventorReference,
            organisation.getName().getContent(),
            Timestamp.now());
    }
}
