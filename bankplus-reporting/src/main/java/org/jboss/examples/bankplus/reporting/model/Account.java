package org.jboss.examples.bankplus.reporting.model;

import org.jboss.examples.bankplus.money.model.Money;

import javax.persistence.Embeddable;
import javax.persistence.Transient;

@Embeddable
public class Account {

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
