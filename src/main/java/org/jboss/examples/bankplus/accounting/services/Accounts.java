package org.jboss.examples.bankplus.accounting.services;

import org.jboss.examples.bankplus.accounting.model.Account;
import org.jboss.examples.bankplus.accounting.model.AccountBalanceHistory;
import org.jboss.examples.bankplus.accounting.model.AccountType;
import org.jboss.examples.bankplus.money.model.Money;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Stateless
public class Accounts {

    @PersistenceContext
    private EntityManager em;

    public Account newAccount(String accountId, String name, AccountType accountType, Account parentAccount, Money openingBalance) {
        Account account = new Account();
        account.setAccountId(accountId);
        account.setName(name);
        if (parentAccount != null && accountType != parentAccount.getAccountType()) {
            throw new AccountException("The account type: [" + accountType + "] does not match the parent account's type: [" + parentAccount.getAccountType() + "]");
        }
        account.setAccountType(accountType);
        account.setOpeningBalance(openingBalance);
        account.setCurrentBalance(openingBalance);
        account.setPeriodOpenDate(Date.from(LocalDate.now().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
        account.setParentAccount(parentAccount);

        // Initialize account balance history
        Date now = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
        AccountBalanceHistory balanceHistory = new AccountBalanceHistory();
        balanceHistory.setAccount(account);
        balanceHistory.setDate(now);
        balanceHistory.setOpeningBalance(openingBalance);
        account.getBalanceHistories().add(balanceHistory);
        account.setLastUpdatedOn(now);

        em.persist(account);

        if(accountId == null) {
            accountId = String.format("2%019d", account.getId());
            account.setAccountId(accountId);
            account = em.merge(account);
        }
        return account;
    }

    public Account findByAccountId(final String accountId) {
        TypedQuery<Account> findByIdQuery = em.createQuery("SELECT DISTINCT a FROM Account a WHERE a.accountId = :accountId", Account.class);
        findByIdQuery.setParameter("accountId", accountId);
        try {
            Account account = findByIdQuery.getSingleResult();
            return account;
        } catch (NoResultException noResEx) {
            throw new AccountException("Could not find account with Id.");
        }
    }


    public Account getCashAccount() {
        TypedQuery<Account> findByNameQuery = em.createQuery("SELECT DISTINCT a FROM Account a WHERE UPPER(a.name) = 'CASH'", Account.class);
        Account cashAccount = findByNameQuery.getSingleResult();
        return cashAccount;
    }

    public Account getChargesAccount() {
        TypedQuery<Account> findByNameQuery = em.createQuery("SELECT DISTINCT a FROM Account a WHERE UPPER(a.name) = 'CHARGES'", Account.class);
        Account chargesAccount = findByNameQuery.getSingleResult();
        return chargesAccount;
    }

    public Account getClearingAccount() {
        TypedQuery<Account> findByNameQuery = em.createQuery("SELECT DISTINCT a FROM Account a WHERE UPPER(a.name) = 'CLEARING'", Account.class);
        Account clearingAccount = findByNameQuery.getSingleResult();
        return clearingAccount;
    }

    public Account getLiabilitiesAccount() {
        TypedQuery<Account> findByNameQuery = em.createQuery("SELECT DISTINCT a FROM Account a WHERE UPPER(a.name) = 'LIABILITIES'", Account.class);
        Account liabiltiesAccount = findByNameQuery.getSingleResult();
        return liabiltiesAccount;
    }

    public List<Account> listAll() {
        TypedQuery<Account> findAllAccountsQuery = em.createQuery("SELECT DISTINCT a FROM Account a ORDER BY a.id", Account.class);
        return findAllAccountsQuery.getResultList();
    }

    public List<Account> listLeafAccounts() {
        TypedQuery<Account> findAllLeafAccountsQuery = em.createQuery("SELECT DISTINCT a FROM Account a WHERE a.parentAccount != NULL AND a.childAccounts.size = 0 ORDER BY a.id", Account.class);
        return findAllLeafAccountsQuery.getResultList();
    }
}
