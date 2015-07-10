package org.jboss.examples.bankplus.model.transactions;

import org.jboss.examples.bankplus.model.customer.Customer;
import org.jboss.examples.bankplus.model.messages.IncomingPaymentMessage;
import org.jboss.examples.bankplus.model.money.Money;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class IncomingPayment extends Event {

    private String payer;

    public String getPayer() {
        return payer;
    }

    public void setPayer(String payer) {
        this.payer = payer;
    }

    @ManyToOne
    private Customer payee;

    public Customer getPayee() {
        return payee;
    }

    public void setPayee(Customer payee) {
        this.payee = payee;
    }

    @Embedded
    private Money paymentAmount;

    public Money getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(Money withdrawalAmount) {
        this.paymentAmount = withdrawalAmount;
    }

    @OneToOne
    private IncomingPaymentMessage incomingPaymentMessage;

    public IncomingPaymentMessage getIncomingPaymentMessage() {
        return incomingPaymentMessage;
    }

    public void setIncomingPaymentMessage(IncomingPaymentMessage outgoingPaymentMessage) {
        this.incomingPaymentMessage = outgoingPaymentMessage;
    }
}
