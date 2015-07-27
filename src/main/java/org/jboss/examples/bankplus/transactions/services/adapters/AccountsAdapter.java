package org.jboss.examples.bankplus.transactions.services.adapters;

import org.jboss.examples.bankplus.accounting.services.Accounts;
import org.jboss.examples.bankplus.transactions.model.Account;
import org.jboss.examples.bankplus.transactions.services.translators.AccountTranslator;

import javax.inject.Inject;

public class AccountsAdapter {

    @Inject
    private Accounts accounts;

    @Inject
    private AccountTranslator translator;

    public Account getCashAccount() {
        org.jboss.examples.bankplus.accounting.model.Account cashAccount = accounts.getCashAccount();
        return translator.translate(cashAccount);
    }

    public Account getChargesAccount() {
        org.jboss.examples.bankplus.accounting.model.Account chargesAccount = accounts.getChargesAccount();
        return translator.translate(chargesAccount);
    }

    public Account getClearingAccount() {
        org.jboss.examples.bankplus.accounting.model.Account clearingAccount = accounts.getClearingAccount();
        return translator.translate(clearingAccount);
    }
}
