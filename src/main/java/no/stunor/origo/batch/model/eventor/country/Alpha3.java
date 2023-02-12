package no.stunor.origo.batch.model.eventor.country;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Alpha3")
public class Alpha3 {

    @XmlAttribute(name = "value")
    private String value;

    public Alpha3() {
    }

    public String getValue() {
        return value;
    }
}
