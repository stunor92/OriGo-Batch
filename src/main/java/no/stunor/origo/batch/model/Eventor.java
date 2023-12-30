package no.stunor.origo.batch.model;

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
public class Eventor {
    @DocumentId
    private String id;
    private String name;
    private String federation;
    private String baseUrl;
    private String apiKey;
}