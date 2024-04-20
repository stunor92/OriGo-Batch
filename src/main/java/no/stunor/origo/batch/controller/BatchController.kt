package no.stunor.origo.batch.controller

import no.stunor.origo.batch.api.EventorApiException
import no.stunor.origo.batch.api.EventorService
import no.stunor.origo.batch.data.EventorRepository
import no.stunor.origo.batch.model.Region
import no.stunor.origo.batch.services.OrganisationService
import no.stunor.origo.batch.services.RegionService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.ExecutionException

@RestController
internal class BatchController {
    private val log = LoggerFactory.getLogger(this.javaClass)

    @Autowired
    private lateinit var eventorRepository: EventorRepository

    @Autowired
    private lateinit var eventorService: EventorService

    @Autowired
    private lateinit var regionService: RegionService

    @Autowired
    private lateinit var organisationService: OrganisationService

    @PostMapping("/updateOrganisations")
    fun updateReorganisations() {
        log.info("Start batch job...")

        val eventorList = eventorRepository.findAll().collectList().block()?: listOf()

        for (eventor in eventorList) {
            try {
                log.info("Updating {}.", eventor.name)

                val eventorOrganisations = eventorService.getOrganisations(eventor.baseUrl, eventor.apiKey).organisation
                log.info("Found {} organisations in {}.", eventorOrganisations.size, eventor.name)
                val regions: List<Region> = regionService.updateRegions(eventor, eventorOrganisations)
                organisationService.updateOrganisations(eventor, eventorOrganisations, regions)
            } catch (e: InterruptedException) {
                log.error("Error updating {}.", eventor.name, e)
            } catch (e: ExecutionException) {
                log.error("Error updating {}.", eventor.name, e)
            } catch (e: EventorApiException) {
                log.error("Error updating {}.", eventor.name, e)
            }
        }
    }
}
