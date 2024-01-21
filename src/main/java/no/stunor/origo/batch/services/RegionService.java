package no.stunor.origo.batch.services;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.iof.eventor.Organisation;
import org.springframework.stereotype.Service;

import com.google.cloud.Timestamp;

import lombok.extern.slf4j.Slf4j;
import no.stunor.origo.batch.model.Eventor;
import no.stunor.origo.batch.model.Region;
    
@Slf4j
@Service
public record RegionService(FirestoreService firestoreService) {  

     public List<Region> updateRegions(Eventor eventor, List<Organisation>  organisations) throws InterruptedException, ExecutionException{
        log.info("Start update regions...");
        List<Region> regions = new ArrayList<>();
        for(Organisation organisation : organisations){
            if(!organisation.getOrganisationTypeId().getContent().equals("2")){
                continue;
            }
            Region region = createRegion(organisation, eventor);
            Region exisitingRegion = firestoreService.getRegion(eventor, region.getOrganisationId());
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

    public static Region createRegion(Organisation organisation, Eventor eventor){
        return new Region(
            null,
            organisation.getOrganisationId().getContent(),
            eventor.getEventorId(),
            organisation.getName().getContent(),
            Timestamp.now());
    }
}
