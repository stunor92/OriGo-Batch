package no.stunor.origo
import no.stunor.origo.batch.Application
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(classes = [Application::class])
class ApplicationTests {

    @Test
    fun contextLoads() {
    }

}
