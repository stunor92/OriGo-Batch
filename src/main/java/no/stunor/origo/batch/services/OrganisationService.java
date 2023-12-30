package no.stunor.origo.batch.services;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.stereotype.Service;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentReference;

import lombok.extern.slf4j.Slf4j;
import no.stunor.origo.batch.model.Organisation;
import no.stunor.origo.batch.model.Region;
    
@Slf4j
@Service
public record OrganisationService(FirestoreService firestoreService) {    


     public void updateOerganisations(DocumentReference eventorReference, List<org.iof.eventor.Organisation>  organisations, List<Region> regions) throws InterruptedException, ExecutionException{
        log.info("Start update organisations...");
        for(org.iof.eventor.Organisation eventorOrganisation : organisations){
            String parentOrganisation =  eventorOrganisation.getParentOrganisation() != null && eventorOrganisation.getParentOrganisation().getOrganisationId() != null ? eventorOrganisation.getParentOrganisation().getOrganisationId().getContent() : null;
            DocumentReference regionReference = firestoreService.getRegionReference(eventorReference, parentOrganisation);
        
            Organisation organisation = createOrganisation(eventorOrganisation, eventorReference, regionReference);
            Organisation exisitingOrganisation = firestoreService.getOrganisation(eventorReference, organisation.getOrganisationNumber());
            if(exisitingOrganisation == null){
                firestoreService.createOrganisation(organisation);
            } else {
                organisation.setId(exisitingOrganisation.getId());
                firestoreService.updateOrganisation(organisation);
            }
        }
        log.info("Finished update of {} organisations.", organisations.size());
    }

    private static Organisation createOrganisation(org.iof.eventor.Organisation organisation, DocumentReference eventorReference, DocumentReference regionReference){

        return new Organisation(
            null,
            organisation.getOrganisationId().getContent(), 
            eventorReference,
            organisation.getName().getContent(),
            organisation.getAddress() != null && !organisation.getAddress().isEmpty() ? organisation.getAddress().get(0).getCareOf() : null,
            organisation.getTele() != null && ! organisation.getTele().isEmpty() ? organisation.getTele().get(0).getMailAddress() : null,
            convertOrganisationType(organisation), 
            regionReference, 
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
}