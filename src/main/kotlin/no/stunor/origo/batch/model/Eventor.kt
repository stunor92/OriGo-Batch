package no.stunor.origo.batch.model

import org.springframework.data.annotation.Id

data class Eventor (
        @Id
        var eventorId: String = "",
        var name: String = "",
        var federation: String = "",
        var baseUrl: String = "",
        var apiKey: String = ""
)