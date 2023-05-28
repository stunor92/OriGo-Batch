package no.stunor.origo.batch.model.eventor;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Tele")
public class Tele {
    @XmlAttribute(name = "mailAddress")
    private String mailAddress;

    public Tele() {
    }

    public String getMailAddress() {
        return mailAddress;
    } 
}