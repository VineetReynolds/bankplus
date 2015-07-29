package org.jboss.examples.bankplus.transactions.services.translators;

import org.jboss.examples.bankplus.transactions.model.Contact;

public class ContactTranslator {

    public Contact translate(org.jboss.examples.bankplus.customer.model.Contact container) {
        Contact contact = null;
        if(container != null) {
            contact = new Contact();
            contact.setContactReference(Long.toString(container.getId()));
            contact.setFullName(container.getFullName());
            contact.setIban(container.getIban());
        }
        return contact;
    }

}
