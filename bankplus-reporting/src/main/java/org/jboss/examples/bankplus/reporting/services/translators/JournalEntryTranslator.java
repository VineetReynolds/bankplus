package org.jboss.examples.bankplus.reporting.services.translators;

import org.jboss.examples.bankplus.reporting.model.Account;
import org.jboss.examples.bankplus.reporting.services.adapters.JournalEntry;

public class JournalEntryTranslator {

    public org.jboss.examples.bankplus.reporting.model.JournalEntry translate(JournalEntry journalEntry) {
        org.jboss.examples.bankplus.reporting.model.JournalEntry model = null;
        if(journalEntry != null) {
            model = new org.jboss.examples.bankplus.reporting.model.JournalEntry();
            model.setId(journalEntry.getId());
            Account modelAccount = new Account();
            modelAccount.setAccountReference(journalEntry.getAccountId());
            model.setAccount(modelAccount);
            model.setAmount(journalEntry.getAmount());
            model.setDateTime(journalEntry.getDateTime());
            model.setDescription(journalEntry.getDescription());
            model.setEventReference(journalEntry.getEventReference());
            model.setType(journalEntry.getType());
        }
        return model;
    }
}
