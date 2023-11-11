package no.stunor.origo.batch.services;

import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.annotation.Nonnull;

import org.springframework.stereotype.Service;

import com.google.cloud.Timestamp;

import lombok.extern.slf4j.Slf4j;
import no.stunor.origo.batch.model.Eventor;
import no.stunor.origo.batch.model.Organisation;
import no.stunor.origo.batch.model.Region;
    
@Slf4j
@Service
public record OrganisationService(FirestoreService firestoreService) {    


     public void updateOerganisations(Eventor eventor, List<org.iof.eventor.Organisation>  organisations, List<Region> regions) throws InterruptedException, ExecutionException{
        log.info("Start update organisations in {}.", eventor.getName());
        for(org.iof.eventor.Organisation organisation : organisations){
            Organisation newOrganisation = createOrganisation(organisation, eventor, regions);
            firestoreService.updateOrganisation(newOrganisation);
        }
        log.info("Finished update of {} regions in {}.", regions.size(), eventor.getName());
    }

     private static Organisation createOrganisation(org.iof.eventor.Organisation organisation, Eventor eventor, List<Region> regions){
        @Nonnull String id = eventor.getId() + "-" + organisation.getOrganisationId().getContent();

        return new Organisation(
            id,
            organisation.getOrganisationId().getContent(), 
            eventor.getId(),
            organisation.getName().getContent(),
            organisation.getAddress() != null && !organisation.getAddress().isEmpty() ? organisation.getAddress().get(0).getCareOf() : null,
            organisation.getTele() != null && ! organisation.getTele().isEmpty() ? organisation.getTele().get(0).getMailAddress() : null,
            convertOrganisationType(organisation), 
            findRegionId(organisation, regions), 
            organisation.getCountry() != null ? organisation.getCountry().getAlpha3().getValue() :null,
            Timestamp.now());
    }

    public static String convertOrganisationType(org.iof.eventor.Organisation organisation){

        switch (organisation.getOrganisationTypeId().getContent()){
            case "1": return "FEDERATION";
            case "2": return "REGION";
            case "5": return "IOF";
            default:  return "CLUB";
        }
    }

    public static String findRegionId(org.iof.eventor.Organisation organisation, List<Region> regions){
        if(organisation.getParentOrganisation() != null && organisation.getParentOrganisation().getOrganisationId() != null){
            for (Region region : regions){
                if(region.getOrganisationNumber().equals(organisation.getParentOrganisation().getOrganisationId().getContent())){
                    return region.getId();
                }
            }
            return null;
        }
        return null;
    }
}