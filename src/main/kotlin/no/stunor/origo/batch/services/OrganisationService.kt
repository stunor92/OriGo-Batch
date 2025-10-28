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
        val regions = regionRepository.findAllByEventorId(eventor.eventorId)
        val organisations: MutableList<Organisation> = ArrayList()
        for (eventorOrganisation in eventorOrganisations) {
            val parentOrganisation: String? =
                if (eventorOrganisation.parentOrganisation != null && eventorOrganisation.parentOrganisation.organisationId != null) eventorOrganisation.parentOrganisation.organisationId.content else null

            val organisation = createOrganisation(eventorOrganisation, eventor)
            for (region in regions) {
                if (region.eventorRef == parentOrganisation) {
                    organisation.regionId = region.id
                    break
                }
            }
            organisations.add(organisation)
        }

        val existingOrganisations = organisationRepository.findAllByEventorId(eventor.eventorId)
        val deletedOrganisations = existingOrganisations.filter { !organisations.contains(it) }

        organisationRepository.deleteAll(deletedOrganisations)
        log.info("Deleted {} organisations.", deletedOrganisations.size)

        organisations.removeAll(deletedOrganisations)
        val updatedOrganisations: MutableList<Organisation> = ArrayList()

        for (organisation in organisations) {
            if(existingOrganisations.contains(organisation) &&
                organisation.isUpdatedAfter(existingOrganisations.first { it.id == organisation.id })) {
                val o = existingOrganisations.first { it.id == organisation.id }
                organisation.eventorApiKey = o.eventorApiKey
                updatedOrganisations.add(organisation)
            } else if (!existingOrganisations.contains(organisation)) {
                updatedOrganisations.add(organisation)
            }
        }
        organisationRepository.saveAll(updatedOrganisations)
        log.info("Finished update of {} organisations.", updatedOrganisations.size)
    }


    private fun createOrganisation(organisation: org.iof.eventor.Organisation, eventor: Eventor): Organisation {
        return Organisation(
            eventorRef = organisation.organisationId.content,
            eventorId = eventor.eventorId,
            name = organisation.name.content,
            contactPerson = if (organisation.getAddress() != null && organisation.address.isNotEmpty()) organisation.address[0].careOf else null,
            email = if (organisation.getTele() != null && organisation.tele.isNotEmpty()) organisation.tele[0].mailAddress else null,
            type = convertOrganisationType(organisation),
            eventorApiKey = null,
            regionId = null,
            country = if(organisation.country.alpha3.value.length == 3) organisation.country.alpha3.value else eventor.eventorId,
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
