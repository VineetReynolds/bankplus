package org.jboss.examples.bankplus.transactions.services.adapters;

import org.jboss.examples.bankplus.transactions.services.client.Contact;
import org.jboss.examples.bankplus.transactions.services.translators.ContactTranslator;

import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

public class ContactsAdapter {

    @Inject
    private ContactTranslator translator;

    public org.jboss.examples.bankplus.transactions.model.Contact findById(Long contactId, Long customerId) {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target("http://localhost:10080/bankplus-customers/rest/").path("customers/{customerId}/contacts/{contactId}")
                .resolveTemplate("customerId", customerId)
                .resolveTemplate("contactId", contactId);
        Contact contact = target.request(MediaType.APPLICATION_JSON_TYPE).get(Contact.class);
        return translator.translate(contact);
    }
}
