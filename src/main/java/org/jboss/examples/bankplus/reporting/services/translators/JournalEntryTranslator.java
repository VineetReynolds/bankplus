package org.jboss.examples.bankplus.reporting.services.translators;

import org.jboss.examples.bankplus.accounting.rest.representations.JournalEntry;
import org.jboss.examples.bankplus.reporting.model.Account;

public class JournalEntryTranslator {

    public JournalEntry translate(org.jboss.examples.bankplus.reporting.model.JournalEntry model) {
        JournalEntry container = null;
        if(model != null) {
            container = new JournalEntry();
            container.setAccountId(model.getAccount().getAccountReference());
            container.setAmount(model.getAmount());
            container.setDateTime(model.getDateTime());
            container.setDescription(model.getDescription());
            container.setEventReference(model.getEventReference());
            container.setType(model.getType());
        }
        return container;
    }

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
