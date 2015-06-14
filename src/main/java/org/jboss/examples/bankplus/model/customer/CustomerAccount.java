package org.jboss.examples.bankplus.model.customer;

import org.jboss.examples.bankplus.model.accounting.Account;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class CustomerAccount extends Account {

    private String iban;

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    @OneToOne(mappedBy = "customerAccount")
    private Customer customer;

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
