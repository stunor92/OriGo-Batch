package no.stunor.origo.batch.services

import com.google.cloud.Timestamp
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
    fun updateRegions(eventor: Eventor, organisations: List<Organisation>): List<Region> {
        log.info("Start update regions...")

        //Timestamp startTtme = Timestamp.now();
        val regions: MutableList<Region> = ArrayList()
        val existingRegions = regionRepository.findAll().collectList().block()?: listOf()

        for (organisation in organisations) {
            if (organisation.organisationTypeId.content != "2") {
                continue
            }
            val region = createRegion(organisation, eventor)
            if (existingRegions.contains(region)) {
                val r = existingRegions[existingRegions.indexOf(region)]
                region.id = r.id
            }
            regions.add(region)
        }
        regionRepository.saveAll(regions).blockLast()
        log.info("Finished update of {} regions.", regions.size)

        return regions
    }

    companion object {
        fun createRegion(organisation: Organisation, eventor: Eventor): Region {
            return Region(
                    id = null,
                    regionId = organisation.organisationId.content,
                    eventorId = eventor.eventorId,
                    name = organisation.name.content,
                    lastUpdated = Timestamp.now())
        }
    }
}
