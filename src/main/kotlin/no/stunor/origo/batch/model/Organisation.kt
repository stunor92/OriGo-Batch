package no.stunor.origo.batch.model

import jakarta.persistence.*
import java.io.Serializable
import java.sql.Timestamp
import java.time.Instant

data class OrganisationId(
    private val organisationId: String,
    private val eventorId: String
) : Serializable{
    constructor() : this("", "")
}

@Entity
@IdClass(OrganisationId::class)
data class Organisation (
    @Id var organisationId: String = "",
    @Id var eventorId: String = "",
    var name: String = "",
    @Enumerated(EnumType.STRING) var type: OrganisationType = OrganisationType.Club,
    var country: String = "",
    var email: String? = null,
    var eventorApiKey: String? = null,
    var regionId: String? = null,
    var contactPerson: String? = null,
    var lastUpdated: Timestamp = Timestamp.from(Instant.now())
) {
    override fun equals(other: Any?): Boolean {
        if (other is Organisation) {
            return this.eventorId == other.eventorId && (this.organisationId == other.organisationId)
        }
        return false
    }

    fun isUpdatedAfter(other: Organisation): Boolean {
        return this.lastUpdated.after(other.lastUpdated)
    }
    override fun hashCode(): Int {
        var result = organisationId.hashCode()
        result = 31 * result + eventorId.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + type.hashCode()
        result = 31 * result + country.hashCode()
        result = 31 * result + (email?.hashCode() ?: 0)
        result = 31 * result + (eventorApiKey?.hashCode() ?: 0)
        result = 31 * result + (regionId?.hashCode() ?: 0)
        result = 31 * result + (contactPerson?.hashCode() ?: 0)
        return result
    }
}

enum class OrganisationType {
    Club, Region, Federation, IOF
}
