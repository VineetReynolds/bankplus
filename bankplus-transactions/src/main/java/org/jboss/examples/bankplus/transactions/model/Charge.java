package org.jboss.examples.bankplus.transactions.model;

import org.jboss.examples.bankplus.money.model.Money;

import javax.persistence.Embedded;
import javax.persistence.Entity;

@Entity
public class Charge extends Event {

    @Embedded
    private Account collectedFrom;

    public Account getCollectedFrom() {
        return collectedFrom;
    }

    public void setCollectedFrom(Account collectedFrom) {
        this.collectedFrom = collectedFrom;
    }

    @Embedded
    private Money chargeAmount;

    public Money getChargeAmount() {
        return chargeAmount;
    }

    public void setChargeAmount(Money chargeAmount) {
        this.chargeAmount = chargeAmount;
    }
}
