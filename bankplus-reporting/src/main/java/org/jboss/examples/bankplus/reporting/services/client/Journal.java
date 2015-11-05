package org.jboss.examples.bankplus.reporting.services.client;

import org.jboss.examples.bankplus.reporting.model.JournalEntry;
import org.jboss.examples.bankplus.reporting.services.adapters.JournalAdapter;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public class Journal {

    @Inject
    private JournalAdapter adapter;

    public List<JournalEntry> getEntriesForMonth(String accountId) {
        return adapter.getEntriesForMonth(accountId);
    }

    public List<JournalEntry> getEntriesForYear(String accountId) {
        return adapter.getEntriesForYear(accountId);
    }

    public List<JournalEntry> getEntries(String accountId, LocalDate from, LocalDate to) {
        return adapter.getEntries(accountId, from, to);
    }
}
