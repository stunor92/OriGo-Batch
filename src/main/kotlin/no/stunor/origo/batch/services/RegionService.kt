package no.stunor.origo.batch.services

import no.stunor.origo.batch.data.RegionRepository
import no.stunor.origo.batch.model.Eventor
import no.stunor.origo.batch.model.Region
import org.iof.eventor.Organisation
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
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
            regionId = organisation.organisationId.content,
            eventorId = eventor.eventorId,
            name = organisation.name.content,
            lastUpdated = TimestampConverter.convertTimestamp(organisation.modifyDate, eventor)
        )
    }
}

