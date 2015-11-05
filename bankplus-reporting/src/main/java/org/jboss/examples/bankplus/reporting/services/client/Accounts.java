package org.jboss.examples.bankplus.reporting.services.client;


import org.jboss.examples.bankplus.reporting.services.adapters.AccountsAdapter;

import javax.inject.Inject;

public class Accounts {

    @Inject
    private AccountsAdapter adapter;

    public org.jboss.examples.bankplus.reporting.model.Account getCashAccount() {
        return adapter.getCashAccount();
    }

    public org.jboss.examples.bankplus.reporting.model.Account getChargesAccount() {
        return adapter.getChargesAccount();
    }

    public org.jboss.examples.bankplus.reporting.model.Account getClearingAccount() {
        return adapter.getClearingAccount();
    }
}
