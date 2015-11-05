package org.jboss.examples.bankplus.reporting.services.translators;

import org.jboss.examples.bankplus.reporting.model.Account;
import org.jboss.examples.bankplus.reporting.services.client.CustomerAccount;

public class CustomerAccountTranslator {

    public org.jboss.examples.bankplus.reporting.model.CustomerAccount translate(CustomerAccount container) {
        org.jboss.examples.bankplus.reporting.model.CustomerAccount customerAccount = null;
        if(container != null) {
            customerAccount = new org.jboss.examples.bankplus.reporting.model.CustomerAccount();
            Account financialAccount = new Account();
            financialAccount.setAccountReference(container.getAccountId());
            financialAccount.setCurrentBalance(container.getBalance());
            customerAccount.setFinancialAccount(financialAccount);
        }
        return customerAccount;
    }
}
