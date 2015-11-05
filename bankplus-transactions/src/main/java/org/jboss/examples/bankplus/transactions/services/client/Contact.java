package org.jboss.examples.bankplus.transactions.services.client;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement
public class Contact implements Serializable {

    private Long id;
    private String fullName;
    private String iban;

    public Contact() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getFullName() {
        return this.fullName;
    }

    public void setFullName(final String fullName) {
        this.fullName = fullName;
    }

    public String getIban() {
        return this.iban;
    }

    public void setIban(final String iban) {
        this.iban = iban;
    }
}