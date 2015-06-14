package org.jboss.examples.bankplus.model.transactions;

import org.jboss.examples.bankplus.model.customer.Customer;
import org.jboss.examples.bankplus.model.money.Money;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class Deposit extends Event {

    @ManyToOne
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
