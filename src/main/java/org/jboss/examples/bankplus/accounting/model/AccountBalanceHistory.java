package org.jboss.examples.bankplus.accounting.model;

import org.jboss.examples.bankplus.money.model.Money;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AccountBalanceHistory)) return false;

        AccountBalanceHistory that = (AccountBalanceHistory) o;

        if (!account.equals(that.account)) return false;
        if (!date.equals(that.date)) return false;
        return openingBalance.equals(that.openingBalance);

    }

    @Override
    public int hashCode() {
        int result = account.hashCode();
        result = 31 * result + date.hashCode();
        result = 31 * result + openingBalance.hashCode();
        return result;
    }
}
