package org.jboss.examples.bankplus.transactions.rest.representation;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigDecimal;

@XmlRootElement
public class Deposit implements Serializable {

    private BigDecimal amount;

    public Deposit() {
    }

    public Deposit(final org.jboss.examples.bankplus.transactions.model.Deposit entity) {
        if (entity != null) {
            this.amount = entity.getDepositAmount().getAmount();
        }
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}