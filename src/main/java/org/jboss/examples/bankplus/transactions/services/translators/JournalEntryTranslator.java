package org.jboss.examples.bankplus.transactions.services.translators;

import org.jboss.examples.bankplus.accounting.rest.representations.JournalEntry;

import javax.inject.Inject;

public class JournalEntryTranslator {

    @Inject
    private AccountTranslator accountTranslator;

    public JournalEntry translate(org.jboss.examples.bankplus.transactions.model.JournalEntry model) {
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
}
