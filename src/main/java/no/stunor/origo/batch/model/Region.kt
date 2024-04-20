package no.stunor.origo.batch.model;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.annotation.DocumentId;
import com.google.cloud.spring.data.firestore.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collectionName = "regions")
public class Region {
    @DocumentId
    private String id;
    private String regionId;
    private String eventorId;
    private String name;
    private Timestamp lastUpdated;

    @Override
    public boolean equals(Object o) {
        if (o instanceof Region) {
            Region r = (Region) o;
            return this.eventorId.equals(r.eventorId) && this.regionId.equals(r.regionId);
        }
        return false;
    }

}
