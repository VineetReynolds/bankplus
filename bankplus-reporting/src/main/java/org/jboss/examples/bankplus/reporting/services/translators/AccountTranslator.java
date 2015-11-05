package org.jboss.examples.bankplus.reporting.services.translators;

import org.jboss.examples.bankplus.reporting.services.client.Account;

public class AccountTranslator {

    public org.jboss.examples.bankplus.reporting.model.Account translate(Account container) {
        org.jboss.examples.bankplus.reporting.model.Account account = null;
        if(container != null) {
            account = new org.jboss.examples.bankplus.reporting.model.Account();
            account.setAccountReference(container.getAccountId());
            account.setCurrentBalance(container.getCurrentBalance());
        }
        return account;
    }
}
