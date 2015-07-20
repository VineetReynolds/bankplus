package org.jboss.examples.bankplus.customer.services;

import org.jboss.examples.bankplus.accounting.model.Account;
import org.jboss.examples.bankplus.accounting.model.AccountBalanceHistory;
import org.jboss.examples.bankplus.accounting.services.Accounts;
import org.jboss.examples.bankplus.customer.model.CustomerAccount;
import org.jboss.examples.bankplus.money.model.Currency;
import org.jboss.examples.bankplus.money.model.Money;
import org.jboss.examples.bankplus.money.services.Currencies;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Stateless
public class CustomerAccounts {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private Currencies currencies;

    @Inject
    private Accounts accounts;


    public CustomerAccount findByIBAN(String iban) {
        TypedQuery<CustomerAccount> findByIBANQuery = em.createQuery("SELECT DISTINCT c FROM CustomerAccount c WHERE c.iban = :iban ORDER BY c.id", CustomerAccount.class);
        findByIBANQuery.setParameter("iban", iban);
        CustomerAccount customerAccount;
        try {
            customerAccount = findByIBANQuery.getSingleResult();
        } catch (NoResultException nre) {
            customerAccount = null;
        }
        return customerAccount;
    }

    public CustomerAccount create(String accountId, String name, String iban) {
        Date now = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
        CustomerAccount customerAccount = new CustomerAccount();
        customerAccount.setAccountId(accountId);
        customerAccount.setName(name);
        Currency USD = currencies.findByCode("USD");
        Money openingBalance = new Money(USD, BigDecimal.ZERO);
        customerAccount.setOpeningBalance(openingBalance);
        customerAccount.setCurrentBalance(openingBalance);
        customerAccount.setIban(iban);
        customerAccount.setPeriodOpenDate(now);
        AccountBalanceHistory balanceHistory = new AccountBalanceHistory();
        balanceHistory.setAccount(customerAccount);
        balanceHistory.setDate(now);
        balanceHistory.setOpeningBalance(openingBalance);
        customerAccount.getBalanceHistories().add(balanceHistory);
        customerAccount.setLastUpdatedOn(now);
        Account liabilitiesAccount = accounts.getLiabilitiesAccount();
        if(liabilitiesAccount == null) {
            throw new CustomerAccountException("Failed to find a parent Liabilities account for the customer account.");
        }
        customerAccount.setParentAccount(liabilitiesAccount);
        em.persist(customerAccount);
        return customerAccount;
    }

    public List<CustomerAccount> listAll() {
        TypedQuery<CustomerAccount> findAllQuery = em.createQuery("SELECT DISTINCT c FROM CustomerAccount c ORDER BY c.id", CustomerAccount.class);
        return findAllQuery.getResultList();
    }
}
