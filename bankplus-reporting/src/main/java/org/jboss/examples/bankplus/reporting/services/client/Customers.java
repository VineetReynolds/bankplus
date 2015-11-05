package org.jboss.examples.bankplus.reporting.services.client;

import org.jboss.examples.bankplus.reporting.services.adapters.CustomerAdapter;

import javax.inject.Inject;

public class Customers {

    @Inject
    private CustomerAdapter adapter;

    public org.jboss.examples.bankplus.reporting.model.Customer findById(Long customerId) {
        return adapter.findById(customerId);
    }

    public org.jboss.examples.bankplus.reporting.model.Customer findByIBAN(String iban) {
        return adapter.findByIBAN(iban);
    }
}
