package org.jboss.examples.bankplus.reporting.model;

import javax.persistence.Embeddable;
import javax.persistence.Transient;

@Embeddable
public class Customer {

    private String customerReference;

    public String getCustomerReference() {
        return customerReference;
    }

    public void setCustomerReference(String customerReference) {
        this.customerReference = customerReference;
    }

    @Transient
    private String fullName;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Transient
    private CustomerAccount customerAccount;

    public CustomerAccount getCustomerAccount() {
        return customerAccount;
    }

    public void setCustomerAccount(CustomerAccount customerAccount) {
        this.customerAccount = customerAccount;
    }
}
