package no.stunor.origo.batch.converter;

import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import no.stunor.origo.batch.model.Eventor;
import no.stunor.origo.batch.model.Organisation;
import no.stunor.origo.batch.model.Region;

@Slf4j
public class OrganisationConverter {

    public static List<Organisation> convertOrganisations(List<org.iof.eventor.Organisation> eventorOrganisations, Eventor eventor, List<Region> regions){
        log.info("Start to convert organisations");
        List<Organisation> organisations = new ArrayList<>();
        for(org.iof.eventor.Organisation eventorOrganisation : eventorOrganisations){
            organisations.add(convertOrganisation(eventorOrganisation, eventor, regions));
        }
        log.info("Finish converting organisations");
        return organisations;
    }
    
    public static Organisation convertOrganisation(org.iof.eventor.Organisation eventorOrganisation, Eventor eventor, List<Region> regions){
        return new Organisation(
            eventorOrganisation.getOrganisationId().getContent(), 
            eventorOrganisation.getName().getContent(),
            eventorOrganisation.getAddress() != null && !eventorOrganisation.getAddress().isEmpty() ? eventorOrganisation.getAddress().get(0).getCareOf() : null,
            eventorOrganisation.getTele() != null && ! eventorOrganisation.getTele().isEmpty() ? eventorOrganisation.getTele().get(0).getMailAddress() : null,
            convertOrganisationType(eventorOrganisation), 
            findRegion(eventorOrganisation, eventor, regions), 
            eventorOrganisation.getCountry() != null ? eventorOrganisation.getCountry().getAlpha3().getValue() :null);
    }
    
    public static String convertOrganisationType(org.iof.eventor.Organisation eventorOrganisation){

        switch (eventorOrganisation.getOrganisationTypeId().getContent()){
            case "1": return "FEDERATION";
            case "2": return "REGION";
            case "5": return "IOF";
            default:  return "CLUB";
        }
    }

    public static String findRegion(org.iof.eventor.Organisation eventorOrganisation, Eventor eventor, List<Region> regions){
        if(eventorOrganisation.getParentOrganisation() != null && eventorOrganisation.getParentOrganisation().getOrganisationId() != null){
            for (Region region : regions){
                if(region.getId().equals(eventorOrganisation.getParentOrganisation().getOrganisationId().getContent())){
                    return region.getId();
                }
            }
            return null;
        }
        return null;
    }
}
