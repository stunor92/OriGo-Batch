package no.stunor.origo.batch.converter;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.Instant;

import lombok.extern.slf4j.Slf4j;
import no.stunor.origo.batch.model.dynamoDb.Eventor;
import no.stunor.origo.batch.model.dynamoDb.Region;
import no.stunor.origo.batch.model.eventor.EventorOrganisation;

@Slf4j
public class RegionConverter {

    public static List<Region> convertRegions(List<EventorOrganisation> eventorOrganisations, Eventor eventor){
        log.info("Start to convert regions");
        List<Region> regions = new ArrayList<>();
        for(EventorOrganisation eventorOrganisation : eventorOrganisations){
            if(eventorOrganisation.getOrganisationTypeId().equals("2")){
                regions.add(convertRegion(eventorOrganisation, eventor));
            }
        }
        return regions;
    }

    public static Region convertRegion(EventorOrganisation eventorOrganisation, Eventor eventor){
        return new Region(
            null, 
            eventorOrganisation.getName(),
            eventorOrganisation.getOrganisationId(),
            eventor.getId(),
            false,
            Instant.now().toString(),
            Instant.now().toString(),
            Instant.now().getMillis(),
            1,
            "Region");
    }
}
