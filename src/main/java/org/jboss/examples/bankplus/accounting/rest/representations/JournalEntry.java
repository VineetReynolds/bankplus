package org.jboss.examples.bankplus.accounting.rest.representations;

import org.jboss.examples.bankplus.accounting.model.Account;
import org.jboss.examples.bankplus.accounting.model.EntryType;
import org.jboss.examples.bankplus.accounting.services.Accounts;
import org.jboss.examples.bankplus.accounting.services.Journal;
import org.jboss.examples.bankplus.money.model.Money;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@XmlRootElement
public class JournalEntry implements Serializable {

    private Long id;
    private String accountId;
    private Money amount;
    private Date dateTime;
    private Long eventReference;
    private String description;
    private EntryType type;

    public JournalEntry() {

    }

    public JournalEntry(org.jboss.examples.bankplus.accounting.model.JournalEntry entry) {
        if(entry != null) {
            this.id = entry.getId();
            this.accountId = entry.getAccount().getAccountId();
            this.amount = entry.getAmount();
            this.dateTime = entry.getDateTime();
            this.eventReference = entry.getEventReference();
            this.description = entry.getDescription();
            this.type = entry.getType();
        }
    }

    public static Set<org.jboss.examples.bankplus.accounting.model.JournalEntry> from(Set<JournalEntry> uploadedJournalEntries, Accounts accounts) {
        Set<org.jboss.examples.bankplus.accounting.model.JournalEntry> entries = new HashSet<>();
        for (JournalEntry uploadedJournalEntry : uploadedJournalEntries) {
            org.jboss.examples.bankplus.accounting.model.JournalEntry entry = from(uploadedJournalEntry, accounts);
            entries.add(entry);
        }
        return entries;
    }

    private static org.jboss.examples.bankplus.accounting.model.JournalEntry from(JournalEntry uploadedJournalEntry, Accounts accounts) {
        org.jboss.examples.bankplus.accounting.model.JournalEntry journalEntry = new org.jboss.examples.bankplus.accounting.model.JournalEntry();
        Account account = accounts.findByAccountId(uploadedJournalEntry.getAccountId());
        journalEntry.setAccount(account);
        journalEntry.setAmount(uploadedJournalEntry.getAmount());
        journalEntry.setEventReference(uploadedJournalEntry.getEventReference());
        journalEntry.setType(uploadedJournalEntry.getType());
        journalEntry.setDateTime(uploadedJournalEntry.getDateTime());
        journalEntry.setDescription(uploadedJournalEntry.getDescription());
        return journalEntry;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
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
