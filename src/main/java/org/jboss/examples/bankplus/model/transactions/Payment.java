package org.jboss.examples.bankplus.model.transactions;

import org.jboss.examples.bankplus.model.customer.Contact;
import org.jboss.examples.bankplus.model.customer.Customer;
import org.jboss.examples.bankplus.model.messages.OutgoingPaymentMessage;
import org.jboss.examples.bankplus.model.money.Money;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class Payment extends Event {

    @ManyToOne
    private Customer payer;

    public Customer getPayer() {
        return payer;
    }

    public void setPayer(Customer withdrawer) {
        this.payer = withdrawer;
    }

    @ManyToOne
    private Contact payee;

    public Contact getPayee() {
        return payee;
    }

    public void setPayee(Contact payee) {
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
    private OutgoingPaymentMessage outgoingPaymentMessage;

    public OutgoingPaymentMessage getOutgoingPaymentMessage() {
        return outgoingPaymentMessage;
    }

    public void setOutgoingPaymentMessage(OutgoingPaymentMessage outgoingPaymentMessage) {
        this.outgoingPaymentMessage = outgoingPaymentMessage;
    }
}
