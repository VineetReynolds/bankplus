package org.jboss.examples.bankplus.model.messages;

import org.jboss.examples.bankplus.model.money.Money;
import org.jboss.examples.bankplus.model.transactions.Payment;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.util.Date;

@XmlRootElement
@Entity
public class IncomingPaymentMessage {

    @Id
    @GeneratedValue
    private Long id;

    private String orderingCustomer;

    private String beneficiary;

    private Money amount;

    private Date bookingDate;

    @Lob
    private String messageText;

    private boolean processed;

    @XmlTransient
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @XmlElement
    public String getOrderingCustomer() {
        return orderingCustomer;
    }

    public void setOrderingCustomer(String orderingCustomer) {
        this.orderingCustomer = orderingCustomer;
    }

    @XmlElement
    public String getBeneficiary() {
        return beneficiary;
    }

    public void setBeneficiary(String beneficiary) {
        this.beneficiary = beneficiary;
    }

    @XmlElement
    public Money getAmount() {
        return amount;
    }

    public void setAmount(Money amount) {
        this.amount = amount;
    }

    @XmlElement
    public Date getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(Date bookingDate) {
        this.bookingDate = bookingDate;
    }

    @XmlTransient
    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    @XmlTransient
    public boolean isProcessed() {
        return processed;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
    }

    public void generate() {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(IncomingPaymentMessage.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            jaxbMarshaller.marshal(this, baos);
            setMessageText(baos.toString());
        } catch (JAXBException jaxbEx) {
            throw new RuntimeException(jaxbEx);
        }
    }

}
