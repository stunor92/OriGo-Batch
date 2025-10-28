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
        val existingRegions = regionRepository.findAllByEventorId(eventor.id)
        val existingByRef = existingRegions.associateBy { it.eventorRef }

        val incomingRegions = organisations
            .filter { it.organisationTypeId.content == "2" }
            .map { org ->
                val lastUpdated = TimestampConverter.convertTimestamp(org.modifyDate, eventor)
                val existing = existingByRef[org.organisationId.content]
                Region(
                    id = existing?.id, // preserve id if exists (update), else null (insert)
                    eventorId = eventor.id,
                    eventorRef = org.organisationId.content,
                    name = org.name.content,
                    lastUpdated = lastUpdated
                )
            }

        val incomingRefs = incomingRegions.map { it.eventorRef }.toSet()
        val deleted = existingRegions.filter { it.eventorRef !in incomingRefs }
        if (deleted.isNotEmpty()) regionRepository.deleteAll(deleted)
        log.info("Deleted {} regions.", deleted.size)

        val toPersist = incomingRegions.filter { inc ->
            val ex = existingByRef[inc.eventorRef]
            ex == null || inc.isUpdatedAfter(ex)
        }
        if (toPersist.isNotEmpty()) regionRepository.saveAll(toPersist)
        log.info("Finished update of {} regions.", toPersist.size)
    }
}
