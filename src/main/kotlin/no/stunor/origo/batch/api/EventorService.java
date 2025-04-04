package no.stunor.origo.batch.api;

import java.util.ArrayList;
import java.util.List;

import org.iof.eventor.OrganisationList;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class EventorService {

    RestTemplate restTemplate;
    public EventorService() {
        restTemplate = new RestTemplate();
        SimpleClientHttpRequestFactory rf = (SimpleClientHttpRequestFactory) restTemplate.getRequestFactory();
        rf.setReadTimeout(6000);
        rf.setConnectTimeout(6000);

        List<HttpMessageConverter<?>> converters = new ArrayList<>();
        converters.add(new Jaxb2RootElementHttpMessageConverter());
        restTemplate.setMessageConverters(converters);
    }

    public OrganisationList getOrganisations(String baseUrl, String apiKey) throws EventorApi {
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
            throw new EventorApi(e.getStatusCode(), e.getStatusText());
        }
    }
}
