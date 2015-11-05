package org.jboss.examples.bankplus.transactions.rest.representation;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigDecimal;

@XmlRootElement
public class Payment implements Serializable {

    private BigDecimal amount;
    private Long payeeId;

    public Payment() {
    }

    public Payment(final org.jboss.examples.bankplus.transactions.model.Payment entity) {
        if (entity != null) {
            this.amount = entity.getPaymentAmount().getAmount();
        }
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