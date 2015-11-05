package org.jboss.examples.bankplus.transactions.rest.representation;

import org.jboss.examples.bankplus.money.model.Money;

import javax.persistence.EntityManager;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigDecimal;

@XmlRootElement
public class IncomingPayment implements Serializable {

    private String orderingCustomer;

    private String beneficiary;

    private Money amount;

    public IncomingPayment() {
    }

    public String getOrderingCustomer() {
        return orderingCustomer;
    }

    public void setOrderingCustomer(String orderingCustomer) {
        this.orderingCustomer = orderingCustomer;
    }

    public String getBeneficiary() {
        return beneficiary;
    }

    public void setBeneficiary(String beneficiary) {
        this.beneficiary = beneficiary;
    }

    public Money getAmount() {
        return amount;
    }

    public void setAmount(Money amount) {
        this.amount = amount;
    }
}