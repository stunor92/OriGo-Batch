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
) : Serializable {
    override fun equals(other: Any?): Boolean {
        if (other is Region) {
            return this.eventorId == other.eventorId && (this.regionId == other.regionId)
        }
        return false
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + eventorId.hashCode()
        result = 31 * result + regionId.hashCode()
        result = 31 * result + name.hashCode()
        return result
    }
}