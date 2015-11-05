package org.jboss.examples.bankplus.messages.rest.representation;

import org.jboss.examples.bankplus.messages.model.OutgoingPaymentMessage;
import org.jboss.examples.bankplus.money.model.Money;

import javax.persistence.EntityManager;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

@XmlRootElement
public class OutgoingPayment implements Serializable {

    private String orderingCustomer;

    private String beneficiary;

    private Money amount;

    private Date bookingDate;

    public OutgoingPaymentMessage fromDTO(OutgoingPaymentMessage entity, EntityManager em){
        if(entity == null)
            entity = new OutgoingPaymentMessage();
        entity.setOrderingCustomer(this.getOrderingCustomer());
        entity.setBeneficiary(this.getBeneficiary());
        entity.setAmount(this.getAmount());
        entity.setBookingDate(this.getBookingDate());
        return entity;
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

    public Date getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(Date bookingDate) {
        this.bookingDate = bookingDate;
    }
}
