package no.stunor.origo.batch.services;


import lombok.extern.slf4j.Slf4j;
import no.stunor.origo.batch.model.Eventor;
import no.stunor.origo.batch.model.Organisation;
import no.stunor.origo.batch.model.Region;
import no.stunor.origo.batch.repository.EventorRepository;
import no.stunor.origo.batch.repository.OrganisationRepository;
import no.stunor.origo.batch.repository.RegionRepository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@Slf4j
@Service
public record DynamoDbService(EventorRepository eventorRepository, OrganisationRepository organisationRepository, RegionRepository regionRepository) {    

    public List<Eventor> getEventorList(){
        log.info("Start to fetch evnetorList.");

        List<Eventor> eventorList = new ArrayList<>();
        eventorRepository.findAll().iterator().forEachRemaining(eventorList::add);;
        
        log.info("Found {} eventor in DynamoDB.", eventorList.size());
        return eventorList;
    }

    public List<Region> getRegionList(){
        log.info("Start to fecth all regions.");

       List<Region> regionList = new ArrayList<>();
        regionRepository.findAll().iterator().forEachRemaining(regionList::add);;
        
        log.info("Found {} regions in DynamoDB.", regionList.size());
        return regionList;
    }

    public List<Organisation> getOrganisationList(){
        log.info("Start to fecth all organisatopns.");

        List<Organisation> organisationList = new ArrayList<>();
        organisationRepository.findAll().iterator().forEachRemaining(organisationList::add);;
        
        log.info("Found {} organisations in DynamoDB.", organisationList.size());
        return organisationList;
    }

    public void updateOrganisation(Organisation organisation){
        organisationRepository.save(organisation);
    }

    public void updateRegion(Region region){
        regionRepository.save(region);
    }
}
