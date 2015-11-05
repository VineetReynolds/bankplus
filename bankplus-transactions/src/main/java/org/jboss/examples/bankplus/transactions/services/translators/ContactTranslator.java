package org.jboss.examples.bankplus.transactions.services.translators;

import org.jboss.examples.bankplus.transactions.services.client.Contact;

public class ContactTranslator {

    public org.jboss.examples.bankplus.transactions.model.Contact translate(Contact container) {
        org.jboss.examples.bankplus.transactions.model.Contact contact = null;
        if(container != null) {
            contact = new org.jboss.examples.bankplus.transactions.model.Contact();
            contact.setContactReference(Long.toString(container.getId()));
            contact.setFullName(container.getFullName());
            contact.setIban(container.getIban());
        }
        return contact;
    }
}
