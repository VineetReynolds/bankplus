package org.jboss.examples.bankplus.accounting.rest.representation;

import org.jboss.examples.bankplus.accounting.model.Account;
import org.jboss.examples.bankplus.accounting.model.EntryType;
import org.jboss.examples.bankplus.accounting.model.JournalEntry;
import org.jboss.examples.bankplus.accounting.services.Accounts;
import org.jboss.examples.bankplus.money.model.Money;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@XmlRootElement
public class JournalEntryRepresentation implements Serializable {

    private AccountRepresentation account;
    private Money amount;
    private Date dateTime;
    private Long eventReference;
    private String description;
    private EntryType type;

    public static Set<JournalEntry> from(Set<JournalEntryRepresentation> uploadedJournalEntries, Accounts accounts) {
        Set<JournalEntry> entries = new HashSet<>();
        for (JournalEntryRepresentation uploadedJournalEntry : uploadedJournalEntries) {
            JournalEntry entry = from(uploadedJournalEntry, accounts);
            entries.add(entry);
        }
        return entries;
    }

    private static JournalEntry from(JournalEntryRepresentation uploadedJournalEntry, Accounts accounts) {
        JournalEntry journalEntry = new JournalEntry();
        Account account = accounts.findByAccountId(uploadedJournalEntry.getAccount().getAccountReference());
        journalEntry.setAccount(account);
        journalEntry.setAmount(uploadedJournalEntry.getAmount());
        journalEntry.setEventReference(uploadedJournalEntry.getEventReference());
        journalEntry.setType(uploadedJournalEntry.getType());
        journalEntry.setDateTime(uploadedJournalEntry.getDateTime());
        journalEntry.setDescription(uploadedJournalEntry.getDescription());
        return journalEntry;
    }

    public AccountRepresentation getAccount() {
        return account;
    }

    public void setAccount(AccountRepresentation account) {
        this.account = account;
    }

    public Money getAmount() {
        return amount;
    }

    public void setAmount(Money amount) {
        this.amount = amount;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public Long getEventReference() {
        return eventReference;
    }

    public void setEventReference(Long eventReference) {
        this.eventReference = eventReference;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public EntryType getType() {
        return type;
    }

    public void setType(EntryType type) {
        this.type = type;
    }


}
