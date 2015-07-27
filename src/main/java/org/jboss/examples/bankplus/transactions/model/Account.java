package org.jboss.examples.bankplus.transactions.model;

import org.jboss.examples.bankplus.money.model.Money;

import javax.persistence.Embeddable;
import javax.persistence.Transient;

@Embeddable
public class Account {

    private Long databaseId;

    public Long getDatabaseId() {
        return databaseId;
    }

    public void setDatabaseId(Long id) {
        this.databaseId = id;
    }


    private String accountReference;

    public String getAccountReference() {
        return accountReference;
    }

    public void setAccountReference(String accountReference) {
        this.accountReference = accountReference;
    }

    @Transient
    private Money currentBalance;

    public Money getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(Money currentBalance) {
        this.currentBalance = currentBalance;
    }
}
