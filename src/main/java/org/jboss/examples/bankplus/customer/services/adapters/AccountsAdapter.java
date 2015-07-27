package org.jboss.examples.bankplus.customer.services.adapters;

import org.jboss.examples.bankplus.accounting.model.AccountType;
import org.jboss.examples.bankplus.accounting.services.Accounts;
import org.jboss.examples.bankplus.customer.model.Account;
import org.jboss.examples.bankplus.customer.services.translators.AccountTranslator;
import org.jboss.examples.bankplus.money.model.Money;

import javax.inject.Inject;

public class AccountsAdapter {

    @Inject
    private Accounts accounts;

    @Inject
    private AccountTranslator translator;

    public Account getLiabilitiesAccount() {
        org.jboss.examples.bankplus.accounting.model.Account liabilitiesAccount = accounts.getLiabilitiesAccount();
        return translator.translate(liabilitiesAccount);
    }

    public Account newAccount(String accountId, String name, AccountType liability, Account liabilitiesAccount, Money openingBalance) {
        org.jboss.examples.bankplus.accounting.model.Account account = accounts.newAccount(accountId, name, liability, translator.translate(liabilitiesAccount), openingBalance);
        return translator.translate(account);
    }

    public Account findByAccountId(String accountId) {
        org.jboss.examples.bankplus.accounting.model.Account account = accounts.findByAccountId(accountId);
        return translator.translate(account);
    }
}
