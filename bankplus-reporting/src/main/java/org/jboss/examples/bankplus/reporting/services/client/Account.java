package org.jboss.examples.bankplus.reporting.services.client;

import org.jboss.examples.bankplus.money.model.Money;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

@XmlRootElement
public class Account implements Serializable {

    private Long id;
    private String accountId;
    private String name;
    private AccountType accountType;
    private Money currentBalance;
    private Date lastUpdatedOn;
    private String parentAccountId;

    public Account() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public Money getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(Money currentBalance) {
        this.currentBalance = currentBalance;
    }

    public Date getLastUpdatedOn() {
        return lastUpdatedOn;
    }

    public void setLastUpdatedOn(Date lastUpdatedOn) {
        this.lastUpdatedOn = lastUpdatedOn;
    }

    public String getParentAccountId() {
        return parentAccountId;
    }

    public void setParentAccountId(String parentAccountId) {
        this.parentAccountId = parentAccountId;
    }
}
