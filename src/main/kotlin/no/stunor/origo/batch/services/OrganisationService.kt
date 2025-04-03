package no.stunor.origo.batch.services

import no.stunor.origo.batch.data.OrganisationRepository
import no.stunor.origo.batch.data.RegionRepository
import no.stunor.origo.batch.model.Eventor
import no.stunor.origo.batch.model.Organisation
import no.stunor.origo.batch.model.OrganisationType
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
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
                if (region.regionId == parentOrganisation) {
                    organisation.regionId = region.regionId
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
                organisation.isUpdatedAfter(existingOrganisations.first { it.organisationId == organisation.organisationId })) {
                val o = existingOrganisations.first { it.organisationId == organisation.organisationId }
                organisation.id = o.id
                organisation.apiKey = o.apiKey
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
            id = null,
            organisationId = organisation.organisationId.content,
            eventorId = eventor.eventorId,
            name = organisation.name.content,
            contactPerson = if (organisation.getAddress() != null && organisation.address.isNotEmpty()) organisation.address[0].careOf else null,
            email = if (organisation.getTele() != null && organisation.tele.isNotEmpty()) organisation.tele[0].mailAddress else null,
            type = convertOrganisationType(organisation),
            apiKey = null,
            regionId = null,
            country = organisation.country.alpha3.value,
            lastUpdated = convertTimestamp(organisation.modifyDate, eventor)
        )
    }

    private fun getTimeZone(eventor: Eventor): ZoneId {
        if (eventor.eventorId == "AUS") {
            return ZoneId.of("Australia/Sydney")
        }
        return ZoneId.of("Europe/Paris")
    }

    private fun parseTimestamp(time: String, eventor: Eventor): ZonedDateTime {
        val sdf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        return ZonedDateTime.parse(time, sdf.withZone(getTimeZone(eventor)))

    }

    private fun convertTimestamp(time: org.iof.eventor.ModifyDate, eventor: Eventor): Timestamp {
        val timeString = time.date.content + " " + time.clock.content
        val zdt = parseTimestamp(timeString, eventor)
        return Timestamp.from(zdt.toInstant())
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
