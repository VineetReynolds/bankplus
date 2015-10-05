package org.jboss.examples.bankplus.customer.services;

import org.jboss.examples.bankplus.customer.model.Account;
import org.jboss.examples.bankplus.customer.model.CustomerAccount;
import org.jboss.examples.bankplus.customer.services.client.Accounts;
import org.jboss.examples.bankplus.money.model.Currency;
import org.jboss.examples.bankplus.customer.rest.representations.Customer;
import org.jboss.examples.bankplus.money.model.Money;
import org.jboss.examples.bankplus.money.services.Currencies;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Stateless
public class CustomerService {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private Currencies currencies;

    @Inject
    private CustomerAccounts customerAccounts;

    @Inject
    private Accounts accounts;

    public org.jboss.examples.bankplus.customer.model.Customer create(Customer dto) {
        org.jboss.examples.bankplus.customer.model.Customer customer = dto.fromDTO(null, em);
        Currency USD = currencies.findByCode("USD");
        CustomerAccount customerAccount = customerAccounts.create("Customer account for " + customer.getFullName(), new Money(USD, BigDecimal.ZERO));
        customer.setCustomerAccount(customerAccount);
        customerAccount.setCustomer(customer);
        em.persist(customer);
        return customer;
    }

    public boolean delete(Long id) {
        org.jboss.examples.bankplus.customer.model.Customer entity = em.find(org.jboss.examples.bankplus.customer.model.Customer.class, id);
        if (entity == null) {
            return false;
        }
        em.remove(entity);
        return true;
    }

    public org.jboss.examples.bankplus.customer.model.Customer findById(Long id) {
        TypedQuery<org.jboss.examples.bankplus.customer.model.Customer> findByIdQuery = em.createQuery("SELECT DISTINCT c FROM Customer c LEFT JOIN FETCH c.contacts LEFT JOIN FETCH c.customerAccount WHERE c.id = :entityId ORDER BY c.id", org.jboss.examples.bankplus.customer.model.Customer.class);
        findByIdQuery.setParameter("entityId", id);
        org.jboss.examples.bankplus.customer.model.Customer entity;
        try {
            entity = findByIdQuery.getSingleResult();
        } catch (NoResultException nre) {
            entity = null;
        }
        Account financialAccount = accounts.findByAccountId(entity.getCustomerAccount().getFinancialAccount().getAccountReference());
        entity.getCustomerAccount().setFinancialAccount(financialAccount);
        return entity;
    }

    public List<org.jboss.examples.bankplus.customer.model.Customer> listAll(Integer startPosition, Integer maxResult, String email) {
        TypedQuery<org.jboss.examples.bankplus.customer.model.Customer> findAllQuery = null;
        if (email == null || email.isEmpty()) {
            findAllQuery = em.createQuery("SELECT DISTINCT c FROM Customer c LEFT JOIN FETCH c.contacts LEFT JOIN FETCH c.customerAccount ORDER BY c.id", org.jboss.examples.bankplus.customer.model.Customer.class);
        } else {
            findAllQuery = em.createQuery("SELECT DISTINCT c FROM Customer c LEFT JOIN FETCH c.contacts LEFT JOIN FETCH c.customerAccount WHERE LOWER(c.emailAddress) = :emailAddress ORDER BY c.id", org.jboss.examples.bankplus.customer.model.Customer.class);
            findAllQuery.setParameter("emailAddress", email.toLowerCase());
        }
        if (startPosition != null) {
            findAllQuery.setFirstResult(startPosition);
        }
        if (maxResult != null) {
            findAllQuery.setMaxResults(maxResult);
        }
        final List<org.jboss.examples.bankplus.customer.model.Customer> searchResults = findAllQuery.getResultList();

        for(org.jboss.examples.bankplus.customer.model.Customer customer: searchResults) {
            Account financialAccount = accounts.findByAccountId(customer.getCustomerAccount().getFinancialAccount().getAccountReference());
            customer.getCustomerAccount().setFinancialAccount(financialAccount);
        }
        return searchResults;
    }

    public org.jboss.examples.bankplus.customer.model.Customer update(Customer dto) {
        org.jboss.examples.bankplus.customer.model.Customer entity = em.find(org.jboss.examples.bankplus.customer.model.Customer.class, dto.getId());
        if (entity == null) {
            return null;
        }
        entity = dto.fromDTO(entity, em);
        try {
            entity = em.merge(entity);
        } catch (OptimisticLockException e) {
            throw new CustomerUpdateException("The entity was updated in a different transaction", e);
        }
        return entity;
    }

    public org.jboss.examples.bankplus.customer.model.Customer findByIBAN(String iban) {
        TypedQuery<org.jboss.examples.bankplus.customer.model.Customer> findByIdQuery = em.createQuery("SELECT DISTINCT c FROM Customer c LEFT JOIN FETCH c.contacts LEFT JOIN c.customerAccount acc WHERE acc.iban = :iban ORDER BY c.id", org.jboss.examples.bankplus.customer.model.Customer.class);
        findByIdQuery.setParameter("iban", iban);
        org.jboss.examples.bankplus.customer.model.Customer entity;
        try {
            entity = findByIdQuery.getSingleResult();
        } catch (NoResultException nre) {
            entity = null;
        }
        return entity;
    }
}
