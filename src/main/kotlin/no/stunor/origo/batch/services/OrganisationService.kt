package no.stunor.origo.batch.services

import no.stunor.origo.batch.data.OrganisationRepository
import no.stunor.origo.batch.data.RegionRepository
import no.stunor.origo.batch.model.Eventor
import no.stunor.origo.batch.model.Organisation
import no.stunor.origo.batch.model.OrganisationType
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.concurrent.ExecutionException

@Service
class OrganisationService {

    private val log = LoggerFactory.getLogger(this.javaClass)

    @Autowired
    private lateinit var organisationRepository: OrganisationRepository
    @Autowired
    private lateinit var regionRepository: RegionRepository

    @Throws(InterruptedException::class, ExecutionException::class)
    fun updateOrganisations(eventor: Eventor, eventorOrganisations: List<org.iof.eventor.Organisation>) {
        log.info("Start update organisations...")
        val regions = regionRepository.findAllByEventorId(eventor.id)
        val existingOrganisations = organisationRepository.findAllByEventorId(eventor.id)
        val existingByRef = existingOrganisations.associateBy { it.eventorRef }

        // Build incoming organisations preserving existing id & api key and assigning regionId
        val incomingOrganisations = eventorOrganisations.map { eventorOrganisation ->
            val parentOrganisation: String? = if (eventorOrganisation.parentOrganisation?.organisationId != null) {
                eventorOrganisation.parentOrganisation.organisationId.content
            } else null
            val org = createOrganisation(eventorOrganisation, eventor)
            // assign regionId by matching parent's ref
            val matchingRegion = regions.firstOrNull { it.eventorRef == parentOrganisation }
            org.regionId = matchingRegion?.id

            // preserve id & api key if already exists
            val existing = existingByRef[org.eventorRef]
            if (existing != null) {
                org.id = existing.id
                org.eventorApiKey = existing.eventorApiKey
            }
            org
        }

        val incomingRefs = incomingOrganisations.map { it.eventorRef }.toSet()
        val deletedOrganisations = existingOrganisations.filter { it.eventorRef !in incomingRefs }
        if (deletedOrganisations.isNotEmpty()) {
            organisationRepository.deleteAll(deletedOrganisations)
        }
        log.info("Deleted {} organisations.", deletedOrganisations.size)

        // Determine which to persist (new or updated by lastUpdated)
        val toPersist = incomingOrganisations.filter { inc ->
            val ex = existingByRef[inc.eventorRef]
            ex == null || inc.isUpdatedAfter(ex)
        }

        if (toPersist.isNotEmpty()) {
            organisationRepository.saveAll(toPersist)
        }
        log.info("Finished update of {} organisations.", toPersist.size)
    }

    private fun createOrganisation(organisation: org.iof.eventor.Organisation, eventor: Eventor): Organisation {
        return Organisation(
            eventorRef = organisation.organisationId.content,
            eventorId = eventor.id,
            name = organisation.name.content,
            contactPerson = if (organisation.getAddress() != null && organisation.address.isNotEmpty()) organisation.address[0].careOf else null,
            email = if (organisation.getTele() != null && organisation.tele.isNotEmpty()) organisation.tele[0].mailAddress else null,
            type = convertOrganisationType(organisation),
            eventorApiKey = null,
            regionId = null,
            country = if (organisation.country.alpha3.value.length == 3) organisation.country.alpha3.value else eventor.id,
            lastUpdated = TimestampConverter.convertTimestamp(organisation.modifyDate, eventor)
        )
    }

    private fun convertOrganisationType(organisation: org.iof.eventor.Organisation): OrganisationType {
        return when (organisation.organisationTypeId.content) {
            "1" -> OrganisationType.Federation
            "2" -> OrganisationType.Region
            "5" -> OrganisationType.IOF
            else -> OrganisationType.Club
        }
    }
}
