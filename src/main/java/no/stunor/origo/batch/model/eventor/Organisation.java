package no.stunor.origo.batch.model.eventor;

import no.stunor.origo.batch.model.eventor.country.Country;
import no.stunor.origo.batch.model.eventor.country.CountryId;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Organisation")
public class Organisation {
    @XmlElement(name = "OrganisationId")
    private String organisationId;

    @XmlElement(name = "Name")
    private String name;

    @XmlElement(name = "ShortName")
    private String shortName;

    @XmlElement(name = "MediaName")
    private String mediaName;

    @XmlElement(name = "OrganisationTypeId")
    private String organisationTypeId;

    @XmlElement(name = "CountryId")
    private CountryId countryId;

    @XmlElement(name = "Country")
    private Country country;

    @XmlElement(name = "ModifyDate")
    private TimeDate modifyDate;

    @XmlElement(name = "OrganisationStatusId")
    private String organisationStatusId;

    @XmlElement(name = "ParentOrganisation")
    private ParentOrganisation parentOrganisation;

    public Organisation() {
    }

    public String getOrganisationId() {
        return organisationId;
    }

    public String getName() {
        return name;
    }

    public String getShortName() {
        return shortName;
    }

    public String getMediaName() {
        return mediaName;
    }

    public String getOrganisationTypeId() {
        return organisationTypeId;
    }

    public CountryId getCountryId() {
        return countryId;
    }

    public Country getCountry() {
        return country;
    }

    public TimeDate getModifyDate() {
        return modifyDate;
    }

    public String getOrganisationStatusId() {
        return organisationStatusId;
    }

    public ParentOrganisation getParentOrganisation() {
        return parentOrganisation;
    }
}
