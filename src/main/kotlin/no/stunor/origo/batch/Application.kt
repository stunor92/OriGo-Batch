package no.stunor.origo.batch

import no.stunor.origo.batch.services.BatchService
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
open class Application private constructor(private val batchService: BatchService) : CommandLineRunner {

    override fun run(vararg args: String?) {
        batchService.updateOrganisations()
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, *args)
}