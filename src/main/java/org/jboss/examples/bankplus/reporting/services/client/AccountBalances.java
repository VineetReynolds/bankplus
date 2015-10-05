package org.jboss.examples.bankplus.reporting.services.client;

import org.jboss.examples.bankplus.reporting.services.adapters.AccountBalanceAdapter;

import javax.inject.Inject;
import java.time.LocalDate;

public class AccountBalances {

    @Inject
    private AccountBalanceAdapter balanceAdapter;


    public AccountBalance getBalance(String accountId, LocalDate from) {
        return balanceAdapter.getBalance(accountId, from);
    }
}
