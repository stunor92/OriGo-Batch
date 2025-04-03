package no.stunor.origo.batch.data

import no.stunor.origo.batch.model.Organisation
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface OrganisationRepository : CrudRepository<Organisation, Long> {
    fun findAllByEventorId(eventorId: String): List<Organisation>
}