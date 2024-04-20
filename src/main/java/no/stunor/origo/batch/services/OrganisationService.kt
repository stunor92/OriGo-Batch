package no.stunor.origo.batch.services;

import java.util.ArrayList;
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

    public void updateOerganisations(Eventor eventor, List<org.iof.eventor.Organisation>  eventorOrganisations, List<Region> regions) throws InterruptedException, ExecutionException{
        log.info("Start update organisations...");
        //Timestamp startTtme = Timestamp.now();
        List<Organisation> exisitingOrganisatons = organisationRepository.findAll().collectList().block();
        List<Organisation> organisations = new ArrayList<>();
        for(org.iof.eventor.Organisation eventorOrganisation : eventorOrganisations){
            String parentOrganisation =  eventorOrganisation.getParentOrganisation() != null && eventorOrganisation.getParentOrganisation().getOrganisationId() != null ? eventorOrganisation.getParentOrganisation().getOrganisationId().getContent() : null;
        
            Organisation organisation = createOrganisation(eventorOrganisation, eventor);
            for(Region region : regions){
                if(region.getRegionId().equals(parentOrganisation)){
                    organisation.setRegionId(region.getRegionId());
                    break;
                }
            }
            if(exisitingOrganisatons.contains(organisation)){
                Organisation o = exisitingOrganisatons.get(exisitingOrganisatons.indexOf(organisation));
                organisation.setId(o.getId());
            }
            organisations.add(organisation);
        }
        organisationRepository.saveAll(organisations).blockLast();

        //organisationRepository.deleteWithLastUpdatedBefore(startTtme);
        log.info("Finished update of {} organisations.", organisations.size());
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