package org.jboss.examples.bankplus.accounting.rest.representations;

import org.jboss.examples.bankplus.accounting.model.AccountBalanceHistory;
import org.jboss.examples.bankplus.money.model.Money;

import java.util.Date;

public class AccountBalance {

    public AccountBalance() {

    }

    public AccountBalance(AccountBalanceHistory balanceHistory) {
        this.accountReference = balanceHistory.getAccount().getAccountId();
        this.balance = balanceHistory.getOpeningBalance();
        this.date = balanceHistory.getDate();
    }

    private String accountReference;

    public String getAccountReference() {
        return accountReference;
    }

    public void setAccountReference(String accountReference) {
        this.accountReference = accountReference;
    }

    private Money balance;

    public Money getBalance() {
        return balance;
    }

    public void setBalance(Money balance) {
        this.balance = balance;
    }

    private Date date;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
