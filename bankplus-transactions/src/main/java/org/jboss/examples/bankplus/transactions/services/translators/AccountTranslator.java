package org.jboss.examples.bankplus.transactions.services.translators;

import org.jboss.examples.bankplus.transactions.services.client.Account;

public class AccountTranslator {

    public org.jboss.examples.bankplus.transactions.model.Account translate(Account container) {
        org.jboss.examples.bankplus.transactions.model.Account account = null;
        if(container != null) {
            account = new org.jboss.examples.bankplus.transactions.model.Account();
            account.setAccountReference(container.getAccountId());
            account.setCurrentBalance(container.getCurrentBalance());
        }
        return account;
    }
}
