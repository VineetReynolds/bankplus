package org.jboss.examples.bankplus.transactions.services.adapters;

import org.jboss.examples.bankplus.transactions.services.client.Contact;
import org.jboss.examples.bankplus.transactions.services.translators.ContactTranslator;

import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

public class ContactsAdapter {

    @Inject
    private ContactTranslator translator;

    private static final String host;

    private static final int port;

    static {
        String envHost = System.getenv("BANKPLUS_CUSTOMERS_SERVICE_HOST");
        host = envHost == null ? "bankplus_customers.dev.docker" : envHost;
        String envPort = System.getenv("BANKPLUS_CUSTOMERS_SERVICE_PORT");
        port = envPort == null ? 8080 : Integer.parseInt(envPort);
    }

    public org.jboss.examples.bankplus.transactions.model.Contact findById(Long contactId, Long customerId) {
        Client client = ClientBuilder.newClient();
        UriBuilder builder = UriBuilder.fromUri("http://{host}:{port}/bankplus-customers/rest/")
                .path("customers/{customerId}/contacts/{contactId}")
                .resolveTemplate("host", host)
                .resolveTemplate("port", port)
                .resolveTemplate("customerId", customerId)
                .resolveTemplate("contactId", contactId);
        WebTarget target = client.target(builder);
        Contact contact = target.request(MediaType.APPLICATION_JSON_TYPE).get(Contact.class);
        return translator.translate(contact);
    }
}
