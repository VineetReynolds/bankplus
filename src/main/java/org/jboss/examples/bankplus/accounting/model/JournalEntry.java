package org.jboss.examples.bankplus.accounting.model;

import org.jboss.examples.bankplus.money.model.Money;
import org.jboss.examples.bankplus.transactions.model.Event;

import javax.persistence.*;
import java.util.Date;

@Entity
public class JournalEntry {

    @Id
    @GeneratedValue
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne
    private Account account;

    public Account getAccount() {
        return account;
    }

    public void setAccount(final Account account) {
        this.account = account;
    }

    @Enumerated(EnumType.STRING)
    private EntryType type;

    public EntryType getType() {
        return type;
    }

    public void setType(final EntryType type) {
        this.type = type;
    }

    @Embedded
    private Money amount;

    public Money getAmount() {
        return amount;
    }

    public void setAmount(Money amount) {
        this.amount = amount;
    }

    @Enumerated(EnumType.STRING)
    private PostingStatus postingStatus;

    public PostingStatus getPostingStatus() {
        return postingStatus;
    }

    public void setPostingStatus(PostingStatus postingStatus) {
        this.postingStatus = postingStatus;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date dateTime;

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    @ManyToOne
    private Event financialEvent;

    public Event getFinancialEvent() {
        return financialEvent;
    }

    public void setFinancialEvent(Event financialEvent) {
        this.financialEvent = financialEvent;
    }
}
