package org.jboss.examples.bankplus.transactions.services.translators;

import org.jboss.examples.bankplus.transactions.model.Account;
import org.jboss.examples.bankplus.transactions.services.client.CustomerAccount;

import javax.inject.Inject;

public class CustomerAccountTranslator {

    @Inject
    private AccountTranslator accountTranslator;

    public org.jboss.examples.bankplus.transactions.model.CustomerAccount translate(CustomerAccount container) {
        org.jboss.examples.bankplus.transactions.model.CustomerAccount customerAccount = null;
        if(container != null) {
            customerAccount = new org.jboss.examples.bankplus.transactions.model.CustomerAccount();
            Account financialAccount = new Account();
            financialAccount.setAccountReference(container.getAccountId());
            financialAccount.setCurrentBalance(container.getBalance());
            customerAccount.setFinancialAccount(financialAccount);
        }
        return customerAccount;
    }
}
