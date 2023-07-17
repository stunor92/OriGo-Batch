package no.stunor.origo.batch.converter;

import java.util.ArrayList;
import java.util.List;

import org.iof.eventor.Organisation;
import org.joda.time.Instant;

import lombok.extern.slf4j.Slf4j;
import no.stunor.origo.batch.model.Eventor;
import no.stunor.origo.batch.model.Region;

@Slf4j
public class RegionConverter {

    public static List<Region> convertRegions(List<Organisation> eventorOrganisations, Eventor eventor){
        log.info("Start to convert regions");
        List<Region> regions = new ArrayList<>();
        for(Organisation eventorOrganisation : eventorOrganisations){
            if(eventorOrganisation.getOrganisationTypeId().getContent().equals("2")){
                regions.add(convertRegion(eventorOrganisation, eventor));
            }
        }
        return regions;
    }

    public static Region convertRegion(Organisation eventorOrganisation, Eventor eventor){
        return new Region(
            null, 
            eventorOrganisation.getName().getContent(),
            eventorOrganisation.getOrganisationId().getContent(),
            eventor.getId(),
            false,
            Instant.now().toString(),
            Instant.now().toString(),
            Instant.now().getMillis(),
            1,
            "Region");
    }
}
