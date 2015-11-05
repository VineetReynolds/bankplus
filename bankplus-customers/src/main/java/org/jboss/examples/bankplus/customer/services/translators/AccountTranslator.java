package org.jboss.examples.bankplus.customer.services.translators;

public class AccountTranslator {

    public org.jboss.examples.bankplus.customer.model.Account translate(org.jboss.examples.bankplus.customer.services.adapters.Account container) {
        org.jboss.examples.bankplus.customer.model.Account account = null;
        if(container != null) {
            account = new org.jboss.examples.bankplus.customer.model.Account();
            account.setAccountReference(container.getAccountId());
            account.setCurrentBalance(container.getCurrentBalance());
            account.setLastUpdatedOn(container.getLastUpdatedOn());
        }
        return account;
    }
}
