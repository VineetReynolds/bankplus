package org.jboss.examples.bankplus.accounting.services;

import org.jboss.examples.bankplus.accounting.model.Account;
import org.jboss.examples.bankplus.accounting.model.AccountBalanceHistory;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Date;

public class AccountBalances {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private Accounts accounts;

    public AccountBalanceHistory getBalance(String accountId, Date date) {
        TypedQuery<AccountBalanceHistory> accountBalanceHistoryQuery = em.createQuery("SELECT DISTINCT bal FROM AccountBalanceHistory bal WHERE bal.account.accountId = :accountId AND bal.date = :date", AccountBalanceHistory.class);
        accountBalanceHistoryQuery.setParameter("accountId", accountId);
        accountBalanceHistoryQuery.setParameter("date", date);

        try{
            AccountBalanceHistory dayOpenBalance = accountBalanceHistoryQuery.getSingleResult();
            return dayOpenBalance;
        } catch (NoResultException noRes) {
            Account financialAccount = accounts.findByAccountId(accountId);
            AccountBalanceHistory openingBalance = new AccountBalanceHistory();
            openingBalance.setAccount(financialAccount);
            openingBalance.setDate(financialAccount.getPeriodOpenDate());
            openingBalance.setOpeningBalance(financialAccount.getOpeningBalance());
            return openingBalance;
        }
    }
}
