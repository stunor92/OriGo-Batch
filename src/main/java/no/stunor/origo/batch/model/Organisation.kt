package no.stunor.origo.batch.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.google.cloud.Timestamp
import com.google.cloud.firestore.annotation.DocumentId
import com.google.cloud.firestore.annotation.ServerTimestamp
import com.google.cloud.spring.data.firestore.Document
import java.io.Serializable

@Document(collectionName = "organisations")
data class Organisation (
        @DocumentId
        var id: String? = null,
        var organisationId: String = "",
        var eventorId: String = "",
        var name: String = "",
        var type: OrganisationType = OrganisationType.CLUB,
        var country: String = "",
        var email: String? = null,
        var apiKey: String? = null,
        var regionId: String? = null,
        var contactPerson: String? = null,
        @ServerTimestamp
        var lastUpdated: Timestamp? = null
) : Serializable {
    override fun equals(other: Any?): Boolean {
        if (other is Organisation) {
            return this.eventorId == other.eventorId && (this.organisationId == other.organisationId)
        }
        return false
    }
    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + organisationId.hashCode()
        result = 31 * result + eventorId.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + type.hashCode()
        result = 31 * result + country.hashCode()
        result = 31 * result + (email?.hashCode() ?: 0)
        result = 31 * result + (apiKey?.hashCode() ?: 0)
        result = 31 * result + (regionId?.hashCode() ?: 0)
        result = 31 * result + (contactPerson?.hashCode() ?: 0)
        result = 31 * result + (lastUpdated?.hashCode() ?: 0)
        return result
    }
}

enum class OrganisationType {
    CLUB, REGION, FEDERATION, IOF
}
