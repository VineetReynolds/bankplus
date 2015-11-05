package org.jboss.examples.bankplus.reporting.services.client;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement
public class Customer implements Serializable {

    private Long id;
    private String fullName;
    private String mailingAddress;
    private String emailAddress;
    private String phoneNumber;
    private String mobileNumber;
    private CustomerAccount account;

    public Customer() {
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

    public String getMailingAddress() {
        return this.mailingAddress;
    }

    public void setMailingAddress(final String mailingAddress) {
        this.mailingAddress = mailingAddress;
    }

    public String getEmailAddress() {
        return this.emailAddress;
    }

    public void setEmailAddress(final String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setPhoneNumber(final String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getMobileNumber() {
        return this.mobileNumber;
    }

    public void setMobileNumber(final String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public CustomerAccount getAccount() {
        return account;
    }

    public void setAccount(CustomerAccount account) {
        this.account = account;
    }
}