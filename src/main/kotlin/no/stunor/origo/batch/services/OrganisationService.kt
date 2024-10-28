package no.stunor.origo.batch.services

import com.google.cloud.Timestamp
import no.stunor.origo.batch.data.OrganisationRepository
import no.stunor.origo.batch.model.Eventor
import no.stunor.origo.batch.model.Organisation
import no.stunor.origo.batch.model.OrganisationType
import no.stunor.origo.batch.model.Region
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.ExecutionException

@Service
class OrganisationService {
    private val log = LoggerFactory.getLogger(this.javaClass)

    @Autowired
    private lateinit var organisationRepository: OrganisationRepository

    @Throws(InterruptedException::class, ExecutionException::class)
    fun updateOrganisations(eventor: Eventor, eventorOrganisations: List<org.iof.eventor.Organisation>, regions: List<Region>) {
        log.info("Start update organisations...")
        val existingOrganisations = organisationRepository.findAll().collectList().block()?: listOf()
        val eventorOrganisationIds = eventorOrganisations.map { it.organisationId.content }.toSet()
        val deletedOrganisations = existingOrganisations.filter { it.organisationId !in eventorOrganisationIds }
        organisationRepository.deleteAll(deletedOrganisations).block()
        val organisations: MutableList<Organisation> = ArrayList()
        for (eventorOrganisation in eventorOrganisations) {
            val parentOrganisation: String? = if (eventorOrganisation.parentOrganisation != null && eventorOrganisation.parentOrganisation.organisationId != null) eventorOrganisation.parentOrganisation.organisationId.content else null

            val organisation = createOrganisation(eventorOrganisation, eventor)
            for (region in regions) {
                if (region.regionId == parentOrganisation) {
                    organisation.regionId = region.regionId
                    break
                }
            }
            if (existingOrganisations.contains(organisation) && existingOrganisations[existingOrganisations.indexOf(organisation)].lastUpdated < organisation.lastUpdated) {
                val r = existingOrganisations[existingOrganisations.indexOf(organisation)]
                organisation.id = r.id
                organisations.add(organisation)
            } else if (!existingOrganisations.contains(organisation)) {
                organisations.add(organisation)
            }
            organisations.add(organisation)
        }
        organisationRepository.saveAll(organisations).blockLast()

        //organisationRepository.deleteWithLastUpdatedBefore(startTtme);
        log.info("Finished update of {} organisations.", organisations.size)
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
        return Timestamp.ofTimeSecondsAndNanos(zdt.toInstant().epochSecond, 0)
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
