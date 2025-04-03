package no.stunor.origo.batch.model

import org.springframework.data.annotation.Id
import java.sql.Timestamp
import java.time.Instant

data class Region (
        @Id
        var id: Long?,
        var eventorId: String = "",
        var regionId: String = "",
        var name: String = "",
        var lastUpdated: Timestamp = Timestamp.from(Instant.now())
) {
    override fun equals(other: Any?): Boolean {
        if (other is Region) {
            return this.eventorId == other.eventorId && (this.regionId == other.regionId)
        }
        return false
    }
    fun isUpdatedAfter(other: Region): Boolean {
        return this.lastUpdated.after(other.lastUpdated)
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + eventorId.hashCode()
        result = 31 * result + regionId.hashCode()
        result = 31 * result + name.hashCode()
        return result
    }
}