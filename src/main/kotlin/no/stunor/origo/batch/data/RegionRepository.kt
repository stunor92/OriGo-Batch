package no.stunor.origo.batch.data

import no.stunor.origo.batch.model.Region
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface RegionRepository : CrudRepository<Region, Long> {
    fun findAllByEventorId(eventorId: String): List<Region>
}