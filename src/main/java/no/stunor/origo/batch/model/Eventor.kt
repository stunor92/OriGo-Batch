package no.stunor.origo.batch.model

import com.google.cloud.spring.data.firestore.Document
import java.io.Serializable

@Document(collectionName = "eventors")
data class Eventor (
        var eventorId: String = "",
        var name: String = "",
        var federation: String = "",
        var baseUrl: String = "",
        var apiKey: String? = null
) : Serializable