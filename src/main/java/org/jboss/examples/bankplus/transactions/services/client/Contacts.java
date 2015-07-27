package org.jboss.examples.bankplus.transactions.services.client;

import org.jboss.examples.bankplus.transactions.model.Contact;
import org.jboss.examples.bankplus.transactions.services.adapters.ContactsAdapter;

import javax.inject.Inject;

public class Contacts {

    @Inject
    private ContactsAdapter adapter;

    public Contact findById(Long payeeId) {
        return adapter.findById(payeeId);
    }
}
