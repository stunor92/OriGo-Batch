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
    private String organisationId;
    private String eventor;
    private String name;
    private Timestamp lastUpdated;
}
