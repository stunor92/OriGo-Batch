package no.stunor.origo.batch.services;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.iof.eventor.Organisation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.cloud.Timestamp;

import lombok.extern.slf4j.Slf4j;
import no.stunor.origo.batch.data.RegionRepository;
import no.stunor.origo.batch.model.Eventor;
import no.stunor.origo.batch.model.Region;
    
@Slf4j
@Service
public class RegionService {

    @Autowired
    RegionRepository regionRepository;

    public List<Region> updateRegions(Eventor eventor, List<Organisation>  organisations) throws InterruptedException, ExecutionException{
        log.info("Start update regions...");
        //Timestamp startTtme = Timestamp.now();

        List<Region> regions = new ArrayList<>();
        List<Region> exisitingRegions = regionRepository.findAll().collectList().block();

        for(Organisation organisation : organisations){
            if(!organisation.getOrganisationTypeId().getContent().equals("2")){
                continue;
            }
            Region region = createRegion(organisation, eventor);
            if(exisitingRegions.contains(region)){
                Region r = exisitingRegions.get(exisitingRegions.indexOf(region));
                region.setId(r.getId());
            }
            regions.add(region);
        }
        regionRepository.saveAll(regions).blockLast();
        log.info("Finished update of {} regions.", regions.size());

        return regions;
    }

    public static Region createRegion(Organisation organisation, Eventor eventor){
        return new Region(
            null,
            organisation.getOrganisationId().getContent(),
            eventor.getEventorId(),
            organisation.getName().getContent(),
            Timestamp.now());
    }
}
