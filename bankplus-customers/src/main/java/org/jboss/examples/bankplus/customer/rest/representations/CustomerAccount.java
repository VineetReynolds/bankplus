package org.jboss.examples.bankplus.customer.rest.representations;

import org.jboss.examples.bankplus.money.model.Money;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.Date;

public class CustomerAccount implements Serializable {

    private String accountId;
    private String iban;
    private Money balance;
    private Date lastUpdatedOn;

    public CustomerAccount() {
    }

    public CustomerAccount(final org.jboss.examples.bankplus.customer.model.CustomerAccount entity) {
        if (entity != null) {
            this.accountId = entity.getFinancialAccount().getAccountReference();
            this.iban = entity.getIban();
            this.balance = entity.getFinancialAccount().getCurrentBalance();
            this.lastUpdatedOn = entity.getFinancialAccount().getLastUpdatedOn();
        }
    }

    public org.jboss.examples.bankplus.customer.model.CustomerAccount fromDTO(org.jboss.examples.bankplus.customer.model.CustomerAccount entity, EntityManager em) {
        throw new IllegalStateException("This is not expected to be invoked.");
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public Money getBalance() {
        return balance;
    }

    public void setBalance(Money balance) {
        this.balance = balance;
    }

    public Date getLastUpdatedOn() {
        return lastUpdatedOn;
    }

    public void setLastUpdatedOn(Date lastUpdatedOn) {
        this.lastUpdatedOn = lastUpdatedOn;
    }
}
