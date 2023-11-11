package no.stunor.origo.batch.model;



import javax.annotation.Nonnull;

import com.google.cloud.Timestamp;
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
 
    @Nonnull
    @DocumentId
    private String id;
    private String organisationNumber;
    private String eventorId;
    private String name;
    private String contactPerson;
    private String email;
    private String type;
    private String regionId;
    private String country;
    private Timestamp lastUpdated;
}