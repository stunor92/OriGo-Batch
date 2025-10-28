package no.stunor.origo.batch.data

import no.stunor.origo.batch.model.Organisation
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface OrganisationRepository : CrudRepository<Organisation, UUID> {
    fun findAllByEventorId(eventorId: String): List<Organisation>
}