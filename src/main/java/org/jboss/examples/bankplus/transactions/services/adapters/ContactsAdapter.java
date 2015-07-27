package org.jboss.examples.bankplus.transactions.services.adapters;

import org.jboss.examples.bankplus.customer.services.Contacts;
import org.jboss.examples.bankplus.transactions.model.Contact;

import javax.inject.Inject;

public class ContactsAdapter {

    @Inject
    private Contacts contacts;

    public Contact findById(Long payeeId) {
        contacts.findById(payeeId);
        return null;
    }
}
