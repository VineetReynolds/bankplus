package org.jboss.examples.bankplus.transactions.services.translators;

import org.jboss.examples.bankplus.transactions.model.Customer;

import javax.inject.Inject;

public class CustomerTranslator {

    @Inject
    private CustomerAccountTranslator customerAccountTranslator;

    public Customer translate(org.jboss.examples.bankplus.customer.model.Customer container) {
        Customer customer = null;
        if(container != null) {
            customer = new Customer();
            customer.setCustomerReference(Long.toString(container.getId()));
            customer.setFullName(container.getFullName());
            customer.setCustomerAccount(customerAccountTranslator.translate(container.getCustomerAccount()));
        }
        return customer;
    }
}
