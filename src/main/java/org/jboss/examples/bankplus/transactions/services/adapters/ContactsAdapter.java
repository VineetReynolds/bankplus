package org.jboss.examples.bankplus.transactions.services.adapters;

import org.jboss.examples.bankplus.customer.services.Contacts;
import org.jboss.examples.bankplus.transactions.model.Contact;
import org.jboss.examples.bankplus.transactions.services.translators.ContactTranslator;

import javax.inject.Inject;

public class ContactsAdapter {

    @Inject
    private Contacts contacts;

    @Inject
    private ContactTranslator translator;

    public Contact findById(Long payeeId) {
        org.jboss.examples.bankplus.customer.model.Contact contact = contacts.findById(payeeId);
        return translator.translate(contact);
    }
}
