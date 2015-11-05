package org.jboss.examples.bankplus.transactions.model;

import javax.persistence.Embeddable;
import javax.persistence.Transient;

@Embeddable
public class Contact {

    @Transient
    public String fullName;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    private String contactReference;

    public String getContactReference() {
        return contactReference;
    }

    public void setContactReference(String customerReference) {
        this.contactReference = customerReference;
    }

    @Transient
    private String iban;

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }
}
