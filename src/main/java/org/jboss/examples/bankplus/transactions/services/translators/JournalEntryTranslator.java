package org.jboss.examples.bankplus.transactions.services.translators;

import org.jboss.examples.bankplus.transactions.model.JournalEntry;

import javax.inject.Inject;

public class JournalEntryTranslator {

    @Inject
    private AccountTranslator accountTranslator;

    public JournalEntry translate(org.jboss.examples.bankplus.accounting.model.JournalEntry container){
        JournalEntry journalEntry = null;
        if(container != null) {
            journalEntry = new JournalEntry();
            journalEntry.setAccount(accountTranslator.translate(container.getAccount()));
            journalEntry.setAmount(container.getAmount());
            journalEntry.setDateTime(container.getDateTime());
            journalEntry.setDescription(container.getDescription());
            journalEntry.setEventReference(container.getEventReference());
            journalEntry.setType(container.getType());
        }
        return journalEntry;
    }

    public org.jboss.examples.bankplus.accounting.model.JournalEntry translate(JournalEntry model) {
        org.jboss.examples.bankplus.accounting.model.JournalEntry container = null;
        if(model != null) {
            container = new org.jboss.examples.bankplus.accounting.model.JournalEntry();
            container.setAccount(accountTranslator.translate(model.getAccount()));
            container.setAmount(model.getAmount());
            container.setDateTime(model.getDateTime());
            container.setDescription(model.getDescription());
            container.setEventReference(model.getEventReference());
            container.setType(model.getType());
        }
        return container;
    }
}
