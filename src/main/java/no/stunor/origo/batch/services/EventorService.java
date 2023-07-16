package no.stunor.origo.batch.services;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import no.stunor.origo.batch.model.eventor.OrganisationList;


@Service
public class EventorService {

    RestTemplate restTemplate;
    public EventorService() {
        restTemplate = new RestTemplate();
        SimpleClientHttpRequestFactory rf = (SimpleClientHttpRequestFactory) restTemplate.getRequestFactory();
        rf.setReadTimeout(6000);
        rf.setConnectTimeout(6000);
        Jaxb2RootElementHttpMessageConverter converter = new Jaxb2RootElementHttpMessageConverter();
        restTemplate.getMessageConverters().set(5, converter);
    }

    public OrganisationList getOrganisations(String baseUrl, String apiKey) throws EventorApiException {
        HttpHeaders headers = new HttpHeaders();
        headers.set("ApiKey", apiKey);

        try {
            HttpEntity<String> request = new HttpEntity<>(headers);
            ResponseEntity<OrganisationList> response = restTemplate.exchange(
                    baseUrl + "api/organisations",
                    HttpMethod.GET,
                    request,
                    OrganisationList.class,
                    1
            );
            return response.getBody();
        } catch (HttpClientErrorException e){
            throw new EventorApiException(e.getStatusCode(), e.getStatusText());
        }
    }
}
