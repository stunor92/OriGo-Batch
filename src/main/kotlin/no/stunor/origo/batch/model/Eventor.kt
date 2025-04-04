package no.stunor.origo.batch.model

import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity
data class Eventor (
        @Id
        var eventorId: String = "",
        var name: String = "",
        var federation: String = "",
        var baseUrl: String = "",
        var apiKey: String = ""
)