package no.stunor.origo.batch.services

import com.google.cloud.Timestamp
import no.stunor.origo.batch.data.RegionRepository
import no.stunor.origo.batch.model.Eventor
import no.stunor.origo.batch.model.Region
import org.iof.eventor.Organisation
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.ExecutionException

@Service
class RegionService {
    private val log = LoggerFactory.getLogger(this.javaClass)

    @Autowired
    private lateinit var regionRepository: RegionRepository

    @Throws(InterruptedException::class, ExecutionException::class)
    fun updateRegions(eventor: Eventor, organisations: List<Organisation>): List<Region> {
        log.info("Start update regions...")

        //Timestamp startTtme = Timestamp.now();
        val regions: MutableList<Region> = ArrayList()
        val existingRegions = regionRepository.findAll().collectList().block()?: listOf()
        val eventorOrganisationIds = organisations.map { it.organisationId.content }.toSet()
        val deletedRegions = existingRegions.filter { it.regionId !in eventorOrganisationIds }
        regionRepository.deleteAll(deletedRegions).block()
        for (organisation in organisations) {
            if (organisation.organisationTypeId.content != "2") {
                continue
            }
            val region = createRegion(organisation, eventor)
            if (existingRegions.contains(region) && existingRegions[existingRegions.indexOf(region)].lastUpdated < region.lastUpdated) {
                val r = existingRegions[existingRegions.indexOf(region)]
                region.id = r.id
                regions.add(region)
            } else if (!existingRegions.contains(region)) {
                regions.add(region)
            }
        }
        regionRepository.saveAll(regions).blockLast()
        log.info("Finished update of {} regions.", regions.size)

        return regions
    }

    private fun createRegion(organisation: Organisation, eventor: Eventor): Region {
        return Region(
            id = null,
            regionId = organisation.organisationId.content,
            eventorId = eventor.eventorId,
            name = organisation.name.content,
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
}

