package no.stunor.origo.batch.converter;

import java.util.ArrayList;
import java.util.List;

import org.iof.eventor.Organisation;

import lombok.extern.slf4j.Slf4j;
import no.stunor.origo.batch.model.Region;

@Slf4j
public class RegionConverter {

    public static List<Region> convertRegions(List<Organisation> eventorOrganisations){
        log.info("Start to convert regions");
        List<Region> regions = new ArrayList<>();
        for(Organisation eventorOrganisation : eventorOrganisations){
            if(eventorOrganisation.getOrganisationTypeId().getContent().equals("2")){
                regions.add(convertRegion(eventorOrganisation));
            }
        }
        return regions;
    }

    public static Region convertRegion(Organisation eventorOrganisation){
        return new Region(
            eventorOrganisation.getOrganisationId().getContent(),
            eventorOrganisation.getName().getContent());
    }
}
