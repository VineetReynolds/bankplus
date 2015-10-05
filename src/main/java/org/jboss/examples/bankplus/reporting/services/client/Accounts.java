package org.jboss.examples.bankplus.reporting.services.client;


import org.jboss.examples.bankplus.transactions.model.Account;
import org.jboss.examples.bankplus.transactions.services.adapters.AccountsAdapter;

import javax.inject.Inject;

public class Accounts {

    @Inject
    private AccountsAdapter adapter;

    public Account getCashAccount() {
        return adapter.getCashAccount();
    }

    public Account getChargesAccount() {
        return adapter.getChargesAccount();
    }

    public Account getClearingAccount() {
        return adapter.getClearingAccount();
    }
}
