package org.jboss.examples.bankplus.transactions.services.translators;

import org.jboss.examples.bankplus.transactions.services.client.Customer;

import javax.inject.Inject;

public class CustomerTranslator {

    @Inject
    private CustomerAccountTranslator customerAccountTranslator;

    public org.jboss.examples.bankplus.transactions.model.Customer translate(Customer container) {
        org.jboss.examples.bankplus.transactions.model.Customer customer = null;
        // The container could be created, but all members may have null values
        if(container != null && container.getId() != null) {
            customer = new org.jboss.examples.bankplus.transactions.model.Customer();
            customer.setCustomerReference(Long.toString(container.getId()));
            customer.setFullName(container.getFullName());
            customer.setCustomerAccount(customerAccountTranslator.translate(container.getAccount()));
        }
        return customer;
    }
}
