package org.jboss.examples.bankplus.reporting.model;

import org.jboss.examples.bankplus.accounting.model.EntryType;
import org.jboss.examples.bankplus.money.model.Money;

import java.util.Date;

public class JournalEntry {

    private Long id;
    private Account account;
    private Money amount;
    private Date dateTime;
    private Long eventReference;
    private String description;
    private EntryType type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Account getAccount() {
        return account;
    }

    public void setAmount(Money amount) {
        this.amount = amount;
    }

    public Money getAmount() {
        return amount;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setEventReference(Long eventReference) {
        this.eventReference = eventReference;
    }

    public Long getEventReference() {
        return eventReference;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setType(EntryType type) {
        this.type = type;
    }

    public EntryType getType() {
        return type;
    }
}
