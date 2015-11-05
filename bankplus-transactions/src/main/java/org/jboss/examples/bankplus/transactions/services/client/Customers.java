package org.jboss.examples.bankplus.transactions.services.client;

import org.jboss.examples.bankplus.transactions.model.Customer;
import org.jboss.examples.bankplus.transactions.services.adapters.CustomerAdapter;

import javax.inject.Inject;

public class Customers {

    @Inject
    private CustomerAdapter adapter;

    public Customer findById(Long customerId) {
        return adapter.findById(customerId);
    }

    public Customer findByIBAN(String iban) {
        return adapter.findByIBAN(iban);
    }
}
