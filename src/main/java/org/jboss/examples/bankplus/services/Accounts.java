package org.jboss.examples.bankplus.services;

import org.jboss.examples.bankplus.model.accounting.Account;
import org.jboss.examples.bankplus.model.accounting.AccountType;
import org.jboss.examples.bankplus.model.money.Currency;
import org.jboss.examples.bankplus.model.money.Money;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.math.BigDecimal;

@Stateless
public class Accounts {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private Currencies currencies;

    public Account newAccount(String accountId, String name, AccountType accountType, Account parentAccount) {
        Account account = new Account();
        account.setAccountId(accountId);
        account.setName(name);
        if (parentAccount != null && accountType != parentAccount.getAccountType()) {
            throw new AccountException("The account type: [" + accountType + "] does not match the parent account's type: [" + parentAccount.getAccountType() + "]");
        }
        account.setAccountType(accountType);
        Currency USD = currencies.findByCode("USD");
        account.setBalance(new Money(USD, BigDecimal.ZERO));
        account.setParentAccount(parentAccount);
        em.persist(account);
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
}
