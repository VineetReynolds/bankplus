package org.jboss.examples.bankplus.rest.dto;

import java.io.Serializable;

import org.jboss.examples.bankplus.model.customer.Customer;

import javax.persistence.EntityManager;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class CustomerDTO implements Serializable {

    private Long id;
    private String fullName;
    private String mailingAddress;
    private String emailAddress;
    private String phoneNumber;
    private String mobileNumber;
    private CustomerAccountDTO account;

    public CustomerDTO() {
    }

    public CustomerDTO(final Customer entity) {
        if (entity != null) {
            this.id = entity.getId();
            this.fullName = entity.getFullName();
            this.mailingAddress = entity.getMailingAddress();
            this.emailAddress = entity.getEmailAddress();
            this.phoneNumber = entity.getPhoneNumber();
            this.mobileNumber = entity.getMobileNumber();
            this.account = new CustomerAccountDTO(entity.getCustomerAccount());
        }
    }

    public Customer fromDTO(Customer entity, EntityManager em) {
        if (entity == null) {
            entity = new Customer();
        }
        entity.setFullName(this.fullName);
        entity.setMailingAddress(this.mailingAddress);
        entity.setEmailAddress(this.emailAddress);
        entity.setPhoneNumber(this.phoneNumber);
        entity.setMobileNumber(this.mobileNumber);
        entity = em.merge(entity);
        return entity;
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

    public CustomerAccountDTO getAccount() {
        return account;
    }

    public void setAccount(CustomerAccountDTO account) {
        this.account = account;
    }
}