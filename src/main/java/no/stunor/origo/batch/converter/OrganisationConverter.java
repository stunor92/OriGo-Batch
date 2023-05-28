package no.stunor.origo.batch.converter;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.Instant;

import lombok.extern.slf4j.Slf4j;
import no.stunor.origo.batch.model.dynamoDb.Eventor;
import no.stunor.origo.batch.model.dynamoDb.Organisation;
import no.stunor.origo.batch.model.dynamoDb.Region;
import no.stunor.origo.batch.model.eventor.EventorOrganisation;

@Slf4j
public class OrganisationConverter {

    public static List<Organisation> convertOrganisations(List<EventorOrganisation> eventorOrganisations, Eventor eventor, List<Region> regions){
        log.info("Start to convert organisations");
        List<Organisation> organisations = new ArrayList<>();
        for(EventorOrganisation eventorOrganisation : eventorOrganisations){
            organisations.add(convertOrganisation(eventorOrganisation, eventor, regions));
        }
        log.info("Finish converting organisations");
        return organisations;
    }
    
    public static Organisation convertOrganisation(EventorOrganisation eventorOrganisation, Eventor eventor, List<Region> regions){
        return new Organisation(
            null, 
            eventorOrganisation.getOrganisationId(), 
            eventorOrganisation.getName(),
            eventorOrganisation.getAddress() != null ? eventorOrganisation.getAddress().getCareOf() : null,
            eventorOrganisation.getTele() != null ? eventorOrganisation.getTele().getMailAddress() : null,
            convertOrganisationType(eventorOrganisation), 
            eventor.getId(),
            findRegion(eventorOrganisation, eventor, regions), 
            eventorOrganisation.getCountry() != null ? eventorOrganisation.getCountry().getAlpha3().getValue() :null,
            Instant.now().toString(),
            Instant.now().toString(),
            Instant.now().getMillis(),
            1,
            "Organisation",
            false);
    }
    
    public static String convertOrganisationType(EventorOrganisation eventorOrganisation){

        switch (eventorOrganisation.getOrganisationTypeId()){
            case "1": return "FEDERATION";
            case "2": return "REGION";
            case "5": return "IOF";
            default:  return "CLUB";
        }
    }

    public static String findRegion(EventorOrganisation eventorOrganisation, Eventor eventor, List<Region> regions){
        if(eventorOrganisation.getParentOrganisation() != null && eventorOrganisation.getParentOrganisation().getOrganisationId() != null){
            for (Region region : regions){
                if(region.getRegionId().equals(eventorOrganisation.getParentOrganisation().getOrganisationId()) && region.getEventorId().equals(eventor.getId())){
                    return region.getId();
                }
            }
            return null;
        }
        return null;
    }
}
