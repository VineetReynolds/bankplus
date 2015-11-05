package org.jboss.examples.bankplus.customer.services;

import org.jboss.examples.bankplus.customer.iban.USIban;
import org.jboss.examples.bankplus.customer.model.Account;
import org.jboss.examples.bankplus.customer.model.CustomerAccount;
import org.jboss.examples.bankplus.customer.services.client.AccountType;
import org.jboss.examples.bankplus.customer.services.client.Accounts;
import org.jboss.examples.bankplus.money.model.Money;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
public class CustomerAccounts {

    @PersistenceContext
    private EntityManager em;

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

    public CustomerAccount create(String name, Money openingBalance) {
        Account liabilitiesAccount = accounts.getLiabilitiesAccount();
        if(liabilitiesAccount == null) {
            throw new CustomerAccountException("Failed to find a parent Liabilities account for the customer account.");
        }
        Account financialAccount = accounts.newAccount(null, name, AccountType.LIABILITY, liabilitiesAccount, openingBalance);

        String iban = new USIban.Builder()
                .bankCode("PLUS")
                .accountNumber(financialAccount.getAccountReference())
                .build()
                .toFormattedString();
        CustomerAccount customerAccount = new CustomerAccount();
        customerAccount.setFinancialAccount(financialAccount);
        customerAccount.setIban(iban);
        em.persist(customerAccount);
        return customerAccount;
    }

    public List<CustomerAccount> listAll() {
        TypedQuery<CustomerAccount> findAllQuery = em.createQuery("SELECT DISTINCT c FROM CustomerAccount c ORDER BY c.id", CustomerAccount.class);
        return findAllQuery.getResultList();
    }
}
