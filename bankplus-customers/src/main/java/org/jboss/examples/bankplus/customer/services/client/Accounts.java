package org.jboss.examples.bankplus.customer.services.client;

import org.jboss.examples.bankplus.customer.model.Account;
import org.jboss.examples.bankplus.customer.services.adapters.AccountsAdapter;
import org.jboss.examples.bankplus.money.model.Money;

import javax.inject.Inject;

public class Accounts {

    @Inject
    private AccountsAdapter adapter;

    public Account getLiabilitiesAccount() {
        return adapter.getLiabilitiesAccount();
    }

    public Account newAccount(String accountId, String name, AccountType liability, Account liabilitiesAccount, Money openingBalance) {
        return adapter.newAccount(accountId, name, liability, liabilitiesAccount, openingBalance);
    }

    public Account findByAccountId(String accountId) {
        return adapter.findByAccountId(accountId);
    }
}
