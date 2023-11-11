package no.stunor.origo.batch.services;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.annotation.Nonnull;

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
        log.info("Start update regions in {}.", eventor.getName());
        List<Region> regions = new ArrayList<>();
        for(Organisation organisation : organisations){
            if(!organisation.getOrganisationTypeId().getContent().equals("2")){
                continue;
            }
            Region region = createRegion(organisation, eventor);
            regions.add(region);
            firestoreService.updateRegion(region);
        }
        log.info("Finished update of {} regions in {}.", regions.size(), eventor.getName());

        return regions;
    }

    public static Region createRegion(Organisation organisation, Eventor eventor){
        @Nonnull String id = eventor.getId() + "-" + organisation.getOrganisationId().getContent();
        return new Region(
            id,
            organisation.getOrganisationId().getContent(),
            eventor.getId(),
            organisation.getName().getContent(),
            Timestamp.now());
    }
}
