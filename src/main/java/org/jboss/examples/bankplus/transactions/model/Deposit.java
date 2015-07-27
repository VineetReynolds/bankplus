package org.jboss.examples.bankplus.transactions.model;

import org.jboss.examples.bankplus.money.model.Money;

import javax.persistence.Embedded;
import javax.persistence.Entity;

@Entity
public class Deposit extends Event {

    @Embedded
    private Customer depositor;

    public Customer getDepositor() {
        return depositor;
    }

    public void setDepositor(Customer depositor) {
        this.depositor = depositor;
    }

    @Embedded
    private Money depositAmount;

    public Money getDepositAmount() {
        return depositAmount;
    }

    public void setDepositAmount(Money depositAmount) {
        this.depositAmount = depositAmount;
    }
}
