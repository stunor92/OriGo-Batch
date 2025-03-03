package no.stunor.origo.batch.model

import com.google.cloud.Timestamp
import com.google.cloud.firestore.annotation.DocumentId
import com.google.cloud.firestore.annotation.ServerTimestamp
import com.google.cloud.spring.data.firestore.Document
import java.io.Serializable

@Document(collectionName = "regions")
data class Region (
        @DocumentId
        var id: String? = null,
        var eventorId: String = "",
        var regionId: String = "",
        var name: String = "",
        var lastUpdated: Timestamp = Timestamp.now()
) : Serializable {
    override fun equals(other: Any?): Boolean {
        if (other is Region) {
            return this.eventorId == other.eventorId && (this.regionId == other.regionId)
        }
        return false
    }
    fun isUpdatedAfter(other: Region): Boolean {
        return this.lastUpdated.toDate().after(other.lastUpdated.toDate())
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + eventorId.hashCode()
        result = 31 * result + regionId.hashCode()
        result = 31 * result + name.hashCode()
        return result
    }
}