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
@Document(collectionName = "organisations")
public class Organisation {
    @DocumentId
    private String id;
    private String organisationId;
    private String eventor;
    private String name;
    private String contactPerson;
    private String email;
    private String type;
    private String region;
    private String country;
    private Timestamp lastUpdated;

    @Override
    public boolean equals(Object o) {
        if (o instanceof Organisation) {
            Organisation org = (Organisation) o;
            return this.eventor.equals(org.eventor) && this.organisationId.equals(org.organisationId);
        }
        return false;
    }
}