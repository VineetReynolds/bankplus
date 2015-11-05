package org.jboss.examples.bankplus.transactions.services.client;

import org.jboss.examples.bankplus.money.model.Money;

import java.io.Serializable;
import java.util.Date;

public class CustomerAccount implements Serializable {

    private String accountId;
    private String iban;
    private Money balance;
    private Date lastUpdatedOn;

    public CustomerAccount() {
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public Money getBalance() {
        return balance;
    }

    public void setBalance(Money balance) {
        this.balance = balance;
    }

    public Date getLastUpdatedOn() {
        return lastUpdatedOn;
    }

    public void setLastUpdatedOn(Date lastUpdatedOn) {
        this.lastUpdatedOn = lastUpdatedOn;
    }
}
