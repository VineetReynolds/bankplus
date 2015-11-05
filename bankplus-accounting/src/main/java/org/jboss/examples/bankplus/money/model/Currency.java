package org.jboss.examples.bankplus.money.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

@Entity
public class Currency {

    @Id
    @XmlAttribute
    private String currencyCode;

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    @XmlTransient
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Currency{" +
                "currencyCode='" + currencyCode + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Currency)) return false;

        Currency currency = (Currency) o;

        if (!currencyCode.equals(currency.currencyCode)) return false;
        return name.equals(currency.name);

    }

    @Override
    public int hashCode() {
        int result = currencyCode.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }
}
