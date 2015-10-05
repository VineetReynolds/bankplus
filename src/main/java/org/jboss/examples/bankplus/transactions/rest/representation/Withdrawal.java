package org.jboss.examples.bankplus.transactions.rest.representation;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigDecimal;

@XmlRootElement
public class Withdrawal implements Serializable {

    private BigDecimal amount;

    public Withdrawal() {
    }

    public Withdrawal(final org.jboss.examples.bankplus.transactions.model.Withdrawal entity) {
        if (entity != null) {
            this.amount = entity.getWithdrawalAmount().getAmount();
        }
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}