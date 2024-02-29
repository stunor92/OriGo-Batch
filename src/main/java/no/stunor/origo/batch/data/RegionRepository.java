package no.stunor.origo.batch.data;


import org.springframework.stereotype.Repository;

import com.google.cloud.spring.data.firestore.FirestoreReactiveRepository;

import no.stunor.origo.batch.model.Region;
import reactor.core.publisher.Mono;

@Repository
public interface RegionRepository extends FirestoreReactiveRepository<Region> {
    Mono<Region> findByOrganisationIdAndEventor(String organisationId, String eventor);
    //void deleteWithLastUpdatedBefore(Timestamp timestamp);
}