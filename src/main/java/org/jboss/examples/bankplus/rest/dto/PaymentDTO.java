package org.jboss.examples.bankplus.rest.dto;

import org.jboss.examples.bankplus.model.transactions.Payment;
import org.jboss.examples.bankplus.model.transactions.Withdrawal;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigDecimal;

@XmlRootElement
public class PaymentDTO implements Serializable {

    private BigDecimal amount;
    private Long payeeId;

    public PaymentDTO() {
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Long getPayeeId() {
        return payeeId;
    }

    public void setPayeeId(Long payeeId) {
        this.payeeId = payeeId;
    }
}