package no.stunor.origo.batch.model;

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
    private String eventorId;
    private String name;
    private String federation;
    private String baseUrl;
    private String apiKey;
}