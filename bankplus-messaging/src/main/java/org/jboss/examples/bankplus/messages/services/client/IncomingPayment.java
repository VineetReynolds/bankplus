package org.jboss.examples.bankplus.messages.services.client;

import org.jboss.examples.bankplus.messages.model.IncomingPaymentMessage;
import org.jboss.examples.bankplus.money.model.Money;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement
public class IncomingPayment implements Serializable {

    private String orderingCustomer;

    private String beneficiary;

    private Money amount;

    public IncomingPayment() {
    }

    public IncomingPayment(IncomingPaymentMessage incomingPaymentMessage) {
        this.orderingCustomer = incomingPaymentMessage.getOrderingCustomer();
        this.beneficiary = incomingPaymentMessage.getBeneficiary();
        this.amount = incomingPaymentMessage.getAmount();
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