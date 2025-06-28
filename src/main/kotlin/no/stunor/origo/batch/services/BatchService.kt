package no.stunor.origo.batch.services

import no.stunor.origo.batch.api.EventorService
import no.stunor.origo.batch.data.EventorRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
internal class BatchService {
    private val log = LoggerFactory.getLogger(this.javaClass)

    @Autowired
    private lateinit var eventorRepository: EventorRepository

    @Autowired
    private lateinit var eventorService: EventorService

    @Autowired
    private lateinit var regionService: RegionService

    @Autowired
    private lateinit var organisationService: OrganisationService

    fun updateOrganisations() {
        log.info("Start batch job...")

        val eventorList = eventorRepository.findAll()

        for (eventor in eventorList) {
            try {
                log.info("Updating {}.", eventor.name)

                val eventorOrganisations = eventorService.getOrganisations(eventor.baseUrl, eventor.eventorApiKey).organisation.toList()
                log.info("Found {} organisations in {}.", eventorOrganisations.size, eventor.name)
                regionService.updateRegions(eventor, eventorOrganisations)
                organisationService.updateOrganisations(eventor, eventorOrganisations)
            } catch(e: Exception) {
                log.error("Error updating {}.", eventor.name, e)
            }
        }
    }
}