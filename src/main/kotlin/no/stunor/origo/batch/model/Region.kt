package no.stunor.origo.batch.model

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.IdClass
import java.io.Serializable
import java.sql.Timestamp
import java.time.Instant

data class RegionId(
    private val regionId: String,
    private val eventorId: String
) : Serializable{
    constructor() : this("", "")
}

@Entity
@IdClass(RegionId::class)
data class Region (
        @Id
        var eventorId: String = "",
        @Id
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
        var result = eventorId.hashCode()
        result = 31 * result + regionId.hashCode()
        result = 31 * result + name.hashCode()
        return result
    }
}