package org.jboss.examples.bankplus.transactions.services.adapters;

import org.jboss.examples.bankplus.accounting.services.Journal;
import org.jboss.examples.bankplus.transactions.model.JournalEntry;
import org.jboss.examples.bankplus.transactions.services.translators.JournalEntryTranslator;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class JournalAdapter {

    @Inject
    private JournalEntryTranslator translator;

    @Inject
    private Journal journal;

    public void recordJournalEntries(Set<JournalEntry> journalEntries) {
        Set<org.jboss.examples.bankplus.accounting.model.JournalEntry> entries = journalEntries.stream().map(translator::translate).collect(Collectors.toSet());
        journal.recordJournalEntries(entries);
    }
}
