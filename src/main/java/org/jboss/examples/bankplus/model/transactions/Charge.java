package org.jboss.examples.bankplus.model.transactions;

import org.jboss.examples.bankplus.model.accounting.Account;
import org.jboss.examples.bankplus.model.money.Money;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class Charge extends Event {

    @ManyToOne
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
