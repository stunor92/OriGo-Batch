package no.stunor.origo.batch.model;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.annotation.DocumentId;

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
public class Organisation {
    @DocumentId
    private String id;
    private String organisationNumber;
    private DocumentReference eventor;
    private String name;
    private String contactPerson;
    private String email;
    private String type;
    private DocumentReference region;
    private String country;
    private Timestamp lastUpdated;
}