package no.stunor.origo.batch.data

import com.google.cloud.spring.data.firestore.FirestoreReactiveRepository
import no.stunor.origo.batch.model.Organisation
import no.stunor.origo.batch.model.Region
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
interface RegionRepository : FirestoreReactiveRepository<Region> {
    fun findAllByEventorId(eventorId: String): Flux<Region>
}