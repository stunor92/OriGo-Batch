package no.stunor.origo.batch.model.eventor;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "OrganisationList")
public class OrganisationList {
    @XmlElement(name = "Organisation")
    private List<EventorOrganisation> organisation = new ArrayList<>();

    public OrganisationList() {
    }

    public List<EventorOrganisation> getOrganisation() {
        return organisation;
    }
    
}

