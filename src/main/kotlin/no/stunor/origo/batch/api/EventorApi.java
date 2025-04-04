package no.stunor.origo.batch.api;

import org.springframework.http.HttpStatusCode;

public class EventorApi extends Throwable {
    final HttpStatusCode statusCode;
    final String statusText;

    public EventorApi(HttpStatusCode statusCode, String statusText) {
        this.statusCode = statusCode;
        this.statusText = statusText;
    }

}