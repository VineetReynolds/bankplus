package org.jboss.examples.bankplus.transactions.services.adapters;

import org.jboss.examples.bankplus.customer.services.CustomerService;
import org.jboss.examples.bankplus.transactions.model.Customer;
import org.jboss.examples.bankplus.transactions.services.translators.CustomerTranslator;

import javax.inject.Inject;

public class CustomerAdapter {

    @Inject
    private CustomerService customerService;

    @Inject
    private CustomerTranslator translator;

    public Customer findById(Long customerId) {
        org.jboss.examples.bankplus.customer.model.Customer customer = customerService.findById(customerId);
        return translator.translate(customer);
    }

    public Customer findByIBAN(String iban) {
        org.jboss.examples.bankplus.customer.model.Customer customer = customerService.findByIBAN(iban);
        return translator.translate(customer);
    }
}
