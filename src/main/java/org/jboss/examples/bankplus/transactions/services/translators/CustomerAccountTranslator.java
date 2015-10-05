package org.jboss.examples.bankplus.transactions.services.translators;

import org.jboss.examples.bankplus.accounting.services.Accounts;
import org.jboss.examples.bankplus.customer.rest.representations.CustomerAccount;
import org.jboss.examples.bankplus.transactions.model.Account;

import javax.inject.Inject;

public class CustomerAccountTranslator {

    @Inject
    private AccountTranslator accountTranslator;

    @Inject
    private Accounts accounts;

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
