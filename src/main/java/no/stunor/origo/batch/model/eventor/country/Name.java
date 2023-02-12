package no.stunor.origo.batch.model.eventor.country;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

@XmlRootElement(name = "Name")
public class Name {
    @XmlAttribute(name = "languageId")
    private String languageId;

    @XmlValue
    private String text;

    public Name() {
    }
}
