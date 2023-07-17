package no.stunor.origo.batch.repository;


import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import no.stunor.origo.batch.model.Region;

@Repository
@EnableScan
public interface RegionRepository extends CrudRepository<Region, String> {
}