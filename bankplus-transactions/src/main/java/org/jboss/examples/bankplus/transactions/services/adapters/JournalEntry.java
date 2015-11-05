package org.jboss.examples.bankplus.transactions.services.adapters;

import org.jboss.examples.bankplus.money.model.Money;
import org.jboss.examples.bankplus.transactions.model.EntryType;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

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
