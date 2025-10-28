package no.stunor.origo.batch.model

import jakarta.persistence.*
import java.sql.Timestamp
import java.time.Instant
import java.util.UUID

@Entity
data class Organisation (
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    var id: UUID? = null,
    var eventorId: String = "",
    var eventorRef: String = "",
    var name: String = "",
    @Enumerated(EnumType.STRING) var type: OrganisationType = OrganisationType.Club,
    var country: String = "",
    var email: String? = null,
    var eventorApiKey: String? = null,
    var regionId: UUID? = null,
    var contactPerson: String? = null,
    var lastUpdated: Timestamp = Timestamp.from(Instant.now())
) {
    override fun equals(other: Any?): Boolean {
        if (other is Organisation) {
            return this.eventorId == other.eventorId && (this.eventorRef == other.eventorId)
        }
        return false
    }

    fun isUpdatedAfter(other: Organisation): Boolean {
        return this.lastUpdated.after(other.lastUpdated)
    }
    override fun hashCode(): Int {
        var result = eventorId.hashCode()
        result = 31 * result + eventorRef.hashCode()
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
