package no.stunor.origo.batch.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.sql.Timestamp
import java.time.Instant
import java.util.UUID

@Entity
data class Region (
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    var id: UUID? = null,
    var eventorId: String = "",
    var eventorRef: String = "",
    var name: String = "",
    var lastUpdated: Timestamp = Timestamp.from(Instant.now())
) {
    override fun equals(other: Any?): Boolean {
        if (other is Region) {
            return this.eventorId == other.eventorId && (this.eventorRef == other.eventorRef)
        }
        return false
    }
    fun isUpdatedAfter(other: Region): Boolean {
        return this.lastUpdated.after(other.lastUpdated)
    }

    override fun hashCode(): Int {
        var result = eventorRef.hashCode()
        result = 31 * result + eventorId.hashCode()
        result = 31 * result + name.hashCode()
        return result
    }
}