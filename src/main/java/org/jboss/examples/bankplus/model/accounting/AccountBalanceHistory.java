package org.jboss.examples.bankplus.model.accounting;

import org.jboss.examples.bankplus.model.money.Money;

import javax.persistence.*;
import java.util.Date;

@Entity
public class AccountBalanceHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
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

    public void setAccount(Account account) {
        this.account = account;
    }

    @Temporal(TemporalType.DATE)
    private Date date;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Embedded
    private Money openingBalance;

    public Money getOpeningBalance() {
        return openingBalance;
    }

    public void setOpeningBalance(Money balance) {
        this.openingBalance = balance;
    }
}
