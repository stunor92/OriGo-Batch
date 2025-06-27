package no.stunor.origo.batch

import no.stunor.origo.batch.services.BatchService
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.ApplicationContext
import kotlin.system.exitProcess

@SpringBootApplication
open class Application private constructor(
    private val batchService: BatchService,
    private val applicationContext: ApplicationContext
) : CommandLineRunner {

    override fun run(vararg args: String?) {
        batchService.updateOrganisations()
        val exitCode = SpringApplication.exit(applicationContext)
        exitProcess(exitCode)
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, *args)
}