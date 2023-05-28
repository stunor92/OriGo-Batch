package no.stunor.origo.batch.model.eventor;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Address")
public class Address {
    @XmlAttribute(name = "careOf")
    private String careOf;

    public Address() {
    }

    public String getCareOf() {
        return careOf;
    }
}