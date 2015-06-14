package org.jboss.examples.bankplus.services;

import org.jboss.examples.bankplus.model.accounting.Account;
import org.jboss.examples.bankplus.model.customer.CustomerAccount;
import org.jboss.examples.bankplus.model.money.Currency;
import org.jboss.examples.bankplus.model.money.Money;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.math.BigDecimal;
import java.util.Date;

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
        CustomerAccount customerAccount = new CustomerAccount();
        customerAccount.setAccountId(accountId);
        customerAccount.setName(name);
        Currency USD = currencies.findByCode("USD");
        customerAccount.setBalance(new Money(USD, BigDecimal.ZERO));
        customerAccount.setIban(iban);
        customerAccount.setLastUpdatedOn(new Date());
        Account liabilitiesAccount = accounts.getLiabilitiesAccount();
        if(liabilitiesAccount == null) {
            throw new CustomerAccountException("Failed to find a parent Liabilities account for the customer account.");
        }
        customerAccount.setParentAccount(liabilitiesAccount);
        em.persist(customerAccount);
        return customerAccount;
    }
}
