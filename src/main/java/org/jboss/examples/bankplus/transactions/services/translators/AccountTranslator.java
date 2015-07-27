package org.jboss.examples.bankplus.transactions.services.translators;

import org.jboss.examples.bankplus.accounting.services.Accounts;
import org.jboss.examples.bankplus.transactions.model.Account;

import javax.inject.Inject;

public class AccountTranslator {

    @Inject
    private Accounts accounts;

    public Account translate(org.jboss.examples.bankplus.accounting.model.Account container) {
        Account account = null;
        if(container != null) {
            account = new Account();
            account.setDatabaseId(container.getId());
            account.setAccountReference(container.getAccountId());
            account.setCurrentBalance(container.getCurrentBalance());
        }
        return account;
    }

    public org.jboss.examples.bankplus.accounting.model.Account translate(Account model) {
        org.jboss.examples.bankplus.accounting.model.Account container = null;
        if(model != null) {
            container = accounts.findByAccountId(model.getAccountReference());
        }
        return container;
    }
}
