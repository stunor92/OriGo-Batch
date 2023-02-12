package no.stunor.origo.batch.services;

public class EventorApiException extends Throwable {
    int statusCode;
    String statusText;

    public EventorApiException(int statusCode, String statusText) {
        this.statusCode = statusCode;
        this.statusText = statusText;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getStatusText() {
        return statusText;
    }
}
