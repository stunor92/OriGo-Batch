package no.stunor.origo.batch.data

import no.stunor.origo.batch.model.Region
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface RegionRepository : CrudRepository<Region, UUID> {
    fun findAllByEventorId(eventorId: String): List<Region>
}