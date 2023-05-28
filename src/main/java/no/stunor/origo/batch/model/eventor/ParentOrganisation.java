package no.stunor.origo.batch.model.eventor;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ParentOrganisation")
public class ParentOrganisation {
    @XmlElement(name = "OrganisationId")
    private String organisationId;

    public ParentOrganisation() {
    }

    public String getOrganisationId() {
        return organisationId;
    }

    
}