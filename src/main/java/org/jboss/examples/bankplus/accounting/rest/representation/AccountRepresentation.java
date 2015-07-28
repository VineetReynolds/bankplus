package org.jboss.examples.bankplus.accounting.rest.representation;

import org.jboss.examples.bankplus.money.model.Money;

public class AccountRepresentation {

    private Long databaseId;

    public Long getDatabaseId() {
        return databaseId;
    }

    public void setDatabaseId(Long id) {
        this.databaseId = id;
    }


    private String accountReference;

    public String getAccountReference() {
        return accountReference;
    }

    public void setAccountReference(String accountReference) {
        this.accountReference = accountReference;
    }

    private Money currentBalance;

    public Money getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(Money currentBalance) {
        this.currentBalance = currentBalance;
    }
}
