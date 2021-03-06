package org.jboss.examples.bankplus.money.model;

import org.jboss.examples.bankplus.money.services.Currencies;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class CurrencyAdapter extends XmlAdapter<String, Currency> {

    // Since @Inject does not work
    private Currencies currencies;

    public String marshal(Currency currency) throws Exception {
        return currency.getCurrencyCode();
    }

    /*
     * XML => Java
     * Given an XML string, use it to build an instance of the unmappable class.
     */
    public Currency unmarshal(String code) throws Exception {
        return currencies.findByCode(code);
    }

    public void setCurrencies(Currencies currencies) {
        this.currencies = currencies;
    }
}
