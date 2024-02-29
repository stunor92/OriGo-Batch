package no.stunor.origo.batch.data;

import org.springframework.stereotype.Repository;

import com.google.cloud.Timestamp;
import com.google.cloud.spring.data.firestore.FirestoreReactiveRepository;

import no.stunor.origo.batch.model.Organisation;
import reactor.core.publisher.Mono;

@Repository
public interface OrganisationRepository extends FirestoreReactiveRepository<Organisation> {
    Mono<Organisation> findByOrganisationIdAndEventor(String organisationId, String eventor);
    void deleteByLastUpdatedBefore(Timestamp timestamp);
}