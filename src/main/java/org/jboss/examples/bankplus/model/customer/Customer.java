package org.jboss.examples.bankplus.model.customer;

import javax.persistence.*;
import java.io.Serializable;
import java.lang.Override;
import java.util.Set;
import java.util.HashSet;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
public class Customer implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Version
    @Column(name = "version")
    private int version;

    @Column
    private String fullName;

    @Column
    private String mailingAddress;

    @Column
    private String emailAddress;

    @Column
    private String phoneNumber;

    @Column
    private String mobileNumber;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "customer")
    private Set<Contact> contacts = new HashSet<Contact>();

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private CustomerAccount customerAccount;

    public Long getId() {
        return this.id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public int getVersion() {
        return this.version;
    }

    public void setVersion(final int version) {
        this.version = version;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getMailingAddress() {
        return mailingAddress;
    }

    public void setMailingAddress(String mailingAddress) {
        this.mailingAddress = mailingAddress;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public Set<Contact> getContacts() {
        return this.contacts;
    }

    public void setContacts(final Set<Contact> contacts) {
        this.contacts = contacts;
    }

    public CustomerAccount getCustomerAccount() {
        return this.customerAccount;
    }

    public void setCustomerAccount(final CustomerAccount customerAccount) {
        this.customerAccount = customerAccount;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Customer)) {
            return false;
        }
        Customer other = (Customer) obj;
        if (id != null) {
            if (!id.equals(other.id)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public String toString() {
        String result = getClass().getSimpleName() + " ";
        if (fullName != null && !fullName.trim().isEmpty())
            result += "fullName: " + fullName;
        if (mailingAddress != null && !mailingAddress.trim().isEmpty())
            result += ", mailingAddress: " + mailingAddress;
        if (emailAddress != null && !emailAddress.trim().isEmpty())
            result += ", emailAddress: " + emailAddress;
        if (phoneNumber != null && !phoneNumber.trim().isEmpty())
            result += ", phoneNumber: " + phoneNumber;
        if (mobileNumber != null && !mobileNumber.trim().isEmpty())
            result += ", mobileNumber: " + mobileNumber;
        return result;
    }
}