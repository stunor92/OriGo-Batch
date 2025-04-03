package no.stunor.origo.batch.services

import no.stunor.origo.batch.data.RegionRepository
import no.stunor.origo.batch.model.Eventor
import no.stunor.origo.batch.model.Region
import org.iof.eventor.Organisation
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.sql.Timestamp
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
    fun updateRegions(eventor: Eventor, organisations: List<Organisation>) {
        log.info("Start update regions...")
        val regions: MutableList<Region> = ArrayList()
        for (organisation in organisations) {
            if (organisation.organisationTypeId.content != "2") {
                continue
            }
            regions.add(createRegion(organisation, eventor))
        }

        val existingRegions = regionRepository.findAllByEventorId(eventor.eventorId)
        val deletedRegions = existingRegions.filter { !regions.contains(it) }
        regionRepository.deleteAll(deletedRegions)
        log.info("Deleted {} regions.", deletedRegions.size)
        regions.removeAll(deletedRegions)


        val updatedRegions: MutableList<Region> = ArrayList()

        for (region in regions) {
            if(existingRegions.contains(region) &&
                region.isUpdatedAfter(existingRegions.first { it.regionId == region.regionId })) {
                val r = existingRegions[existingRegions.indexOf(region)]
                region.id = r.id
                updatedRegions.add(region)
            } else if (!existingRegions.contains(region)) {
                updatedRegions.add(region)
            }
        }
        regionRepository.saveAll(updatedRegions)
        log.info("Finished update of {} regions.", updatedRegions.size)

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
        return Timestamp.from(zdt.toInstant())    }
}

