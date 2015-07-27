package org.jboss.examples.bankplus.transactions.services.client;

import org.jboss.examples.bankplus.transactions.model.JournalEntry;
import org.jboss.examples.bankplus.transactions.services.adapters.JournalAdapter;

import javax.inject.Inject;
import java.util.Set;

public class Journal {

    @Inject
    private JournalAdapter adapter;

    public void recordJournalEntries(Set<JournalEntry> journalEntries) {
        adapter.recordJournalEntries(journalEntries);
    }
}
