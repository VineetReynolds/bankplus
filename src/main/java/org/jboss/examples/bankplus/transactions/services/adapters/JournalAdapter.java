package org.jboss.examples.bankplus.transactions.services.adapters;

import org.jboss.examples.bankplus.transactions.model.JournalEntry;
import org.jboss.examples.bankplus.transactions.services.translators.JournalEntryTranslator;

import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.util.Set;

public class JournalAdapter {

    @Inject
    private JournalEntryTranslator translator;

    public void recordJournalEntries(Set<JournalEntry> journalEntries) {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target("http://localhost:9080/bankplus/rest/").path("journal");

        target.request(MediaType.APPLICATION_JSON_TYPE).post(Entity.json(journalEntries));
    }
}
