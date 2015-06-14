package org.jboss.examples.bankplus.model.accounting;

import org.jboss.examples.bankplus.model.money.Money;

import javax.persistence.*;

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
}
