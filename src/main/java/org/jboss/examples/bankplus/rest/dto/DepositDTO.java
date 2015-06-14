package org.jboss.examples.bankplus.rest.dto;

import org.jboss.examples.bankplus.model.customer.Contact;
import org.jboss.examples.bankplus.model.transactions.Deposit;

import javax.persistence.EntityManager;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigDecimal;

@XmlRootElement
public class DepositDTO implements Serializable {

    private BigDecimal amount;

    public DepositDTO() {
    }

    public DepositDTO(final Deposit entity) {
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