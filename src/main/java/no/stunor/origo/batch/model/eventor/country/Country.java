package no.stunor.origo.batch.model.eventor.country;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "Country")
public class Country {

    @XmlElement(name = "CountryId")
    private CountryId countryId;

    @XmlElement(name = "Alpha3")
    private Alpha3 alpha3;

    @XmlElement(name = "Name")
    private List<Name> names;

    public Country() {
    }

    public CountryId getCountryId() {
        return countryId;
    }

    public Alpha3 getAlpha3() {
        return alpha3;
    }

    public List<Name> getNames() {
        return names;
    }
}
