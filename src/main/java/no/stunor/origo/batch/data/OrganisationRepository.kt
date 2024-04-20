package no.stunor.origo.batch.data

import com.google.cloud.spring.data.firestore.FirestoreReactiveRepository
import no.stunor.origo.batch.model.Organisation
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
interface OrganisationRepository : FirestoreReactiveRepository<Organisation> {
    fun findByOrganisationIdAndEventorId(organisationId: String, eventorId: String): Mono<Organisation>
    //void deleteWithLastUpdatedBefore(Timestamp timestamp);
}