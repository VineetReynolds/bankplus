package org.jboss.examples.bankplus.transactions.services.adapters;

import org.jboss.examples.bankplus.transactions.services.translators.JournalEntryTranslator;

import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import java.util.Set;
import java.util.stream.Collectors;

public class JournalAdapter {

    @Inject
    private JournalEntryTranslator translator;

    private static final String host;

    private static final int port;

    static {
        String envHost = System.getenv("ACCOUNTING_PORT_8080_TCP_ADDR");
        host = envHost == null ? "bankplus_accounting.dev.docker" : envHost;
        String envPort = System.getenv("ACCOUNTING_PORT_8080_TCP_PORT");
        port = envPort == null ? 8080 : Integer.parseInt(envPort);
    }

    public void recordJournalEntries(Set<org.jboss.examples.bankplus.transactions.model.JournalEntry> journalEntries) {
        Set<JournalEntry> uploadedEntries = journalEntries.stream().map(translator::translate).collect(Collectors.toSet());
        Client client = ClientBuilder.newClient();
        UriBuilder builder = UriBuilder.fromUri("http://{host}:{port}/bankplus-accounting/rest/")
                .path("journal")
                .resolveTemplate("host", host)
                .resolveTemplate("port", port);
        WebTarget target = client.target(builder);

        target.request(MediaType.APPLICATION_JSON_TYPE).post(Entity.json(uploadedEntries));
    }
}
