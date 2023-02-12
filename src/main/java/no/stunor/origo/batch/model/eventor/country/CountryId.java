package no.stunor.origo.batch.model.eventor.country;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "CountryId")
public class CountryId {

    @XmlAttribute(name = "value")
    private String value;

    public CountryId() {
    }
}
