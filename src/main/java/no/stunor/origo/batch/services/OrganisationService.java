package no.stunor.origo.batch.services;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.cloud.Timestamp;

import lombok.extern.slf4j.Slf4j;
import no.stunor.origo.batch.data.OrganisationRepository;
import no.stunor.origo.batch.model.Eventor;
import no.stunor.origo.batch.model.Organisation;
import no.stunor.origo.batch.model.Region;
    
@Slf4j
@Service
public class OrganisationService {
    
    @Autowired
    OrganisationRepository organisationRepository;

    public void updateOerganisations(Eventor eventor, List<org.iof.eventor.Organisation>  organisations, List<Region> regions) throws InterruptedException, ExecutionException{
        log.info("Start update organisations...");
        Timestamp startTtme = Timestamp.now();
        for(org.iof.eventor.Organisation eventorOrganisation : organisations){
            String parentOrganisation =  eventorOrganisation.getParentOrganisation() != null && eventorOrganisation.getParentOrganisation().getOrganisationId() != null ? eventorOrganisation.getParentOrganisation().getOrganisationId().getContent() : null;
        
            Organisation organisation = createOrganisation(eventorOrganisation, eventor);
            for(Region region : regions){
                if(region.getOrganisationId().equals(parentOrganisation)){
                    organisation.setRegion(region.getOrganisationId());
                    break;
                }
            }
            Organisation exisitingOrganisation = organisationRepository.findByOrganisationIdAndEventor(organisation.getOrganisationId(), eventor.getEventorId()).block();
            if(exisitingOrganisation == null){
                organisationRepository.save(organisation);
            } else {
                organisation.setId(exisitingOrganisation.getId());
                organisationRepository.save(organisation);
            }
        }
        log.info("Finished update of {} organisations.", organisations.size());
        log.info("Start deleting deleted organisations...");
        organisationRepository.deleteByLastUpdatedBefore(startTtme);
        log.info("Finished deleting deleted organisations.");
    }

    private static Organisation createOrganisation(org.iof.eventor.Organisation organisation, Eventor eventor){

        return new Organisation(
            null,
            organisation.getOrganisationId().getContent(), 
            eventor.getEventorId(),
            organisation.getName().getContent(),
            organisation.getAddress() != null && !organisation.getAddress().isEmpty() ? organisation.getAddress().get(0).getCareOf() : null,
            organisation.getTele() != null && ! organisation.getTele().isEmpty() ? organisation.getTele().get(0).getMailAddress() : null,
            convertOrganisationType(organisation), 
            null, 
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