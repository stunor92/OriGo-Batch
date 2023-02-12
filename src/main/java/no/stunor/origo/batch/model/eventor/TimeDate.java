package no.stunor.origo.batch.model.eventor;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "TimeDate")
public class TimeDate{
    @XmlElement(name = "Date")
    private String Date;

    @XmlElement(name = "Clock")
    private String Clock;

    public TimeDate() {
    }

    public String getDate() {
        return Date;
    }

    public String getClock() {
        return Clock;
    }
}
