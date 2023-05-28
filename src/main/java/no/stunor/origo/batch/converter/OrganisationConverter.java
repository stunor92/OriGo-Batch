package no.stunor.origo.batch.converter;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.Instant;

import lombok.extern.slf4j.Slf4j;
import no.stunor.origo.batch.model.dynamoDb.Eventor;
import no.stunor.origo.batch.model.dynamoDb.Organisation;
import no.stunor.origo.batch.model.eventor.EventorOrganisation;

@Slf4j
public class OrganisationConverter {

    public static List<Organisation> convertOrganisations(List<EventorOrganisation> eventorOrganisations, Eventor eventor){
        log.info("Start to convert organisations");
        List<Organisation> organisations = new ArrayList<>();
        for(EventorOrganisation eventorOrganisation : eventorOrganisations){
            organisations.add(convertOrganisation(eventorOrganisation, eventor));
        }
        log.info("Finish converting organisations");
        return organisations;
    }
    
    public static Organisation convertOrganisation(EventorOrganisation eventorOrganisation, Eventor eventor){
        return new Organisation(
            null, 
            eventorOrganisation.getOrganisationId(), 
            eventorOrganisation.getName(),
            eventorOrganisation.getAddress() != null ? eventorOrganisation.getAddress().getCareOf() : null,
            eventorOrganisation.getTele() != null ? eventorOrganisation.getTele().getMailAddress() : null,
            convertOrganisationType(eventorOrganisation), 
            eventor.getId(),
            eventorOrganisation.getParentOrganisation() != null ? eventorOrganisation.getParentOrganisation().getOrganisationId() : null,
            eventorOrganisation.getCountry() != null ? eventorOrganisation.getCountry().getAlpha3().getValue() :null,
            Instant.now().toString(),
            Instant.now().toString(),
            Instant.now().getMillis(),
            1,
            "Organisation");
    }
    
    public static String convertOrganisationType(EventorOrganisation eventorOrganisation){

        switch (eventorOrganisation.getOrganisationTypeId()){
            case "1": return "FEDERATION";
            case "2": return "REGION";
            case "5": return "IOF";
            default:  return "CLUB";
        }
    }
}
