package org.jboss.examples.bankplus.application;

import org.jboss.examples.bankplus.money.model.Currency;
import org.jboss.examples.bankplus.money.services.Currencies;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class Bootstrap {

    @Inject
    private Currencies currencies;

    public void createAccounts() {
        Currency USD = currencies.create("United States Dollar", "USD");
    }
}
