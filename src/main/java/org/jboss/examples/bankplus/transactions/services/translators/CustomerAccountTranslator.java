package org.jboss.examples.bankplus.transactions.services.translators;

import org.jboss.examples.bankplus.accounting.model.Account;
import org.jboss.examples.bankplus.accounting.services.Accounts;
import org.jboss.examples.bankplus.transactions.model.CustomerAccount;

import javax.inject.Inject;

public class CustomerAccountTranslator {

    @Inject
    private AccountTranslator accountTranslator;

    @Inject
    private Accounts accounts;

    public CustomerAccount translate(org.jboss.examples.bankplus.customer.model.CustomerAccount container) {
        CustomerAccount customerAccount = null;
        if(container != null) {
            customerAccount = new CustomerAccount();
            Account accountContainer = accounts.findByAccountId(container.getFinancialAccount().getAccountReference());
            customerAccount.setFinancialAccount(accountTranslator.translate(accountContainer));
        }
        return customerAccount;
    }
}
