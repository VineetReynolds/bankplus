package org.jboss.examples.bankplus.customer.services;

import org.jboss.examples.bankplus.customer.model.Account;
import org.jboss.examples.bankplus.customer.model.Customer;
import org.jboss.examples.bankplus.customer.model.CustomerAccount;
import org.jboss.examples.bankplus.customer.services.client.Accounts;
import org.jboss.examples.bankplus.money.model.Currency;
import org.jboss.examples.bankplus.customer.rest.dto.CustomerDTO;
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

    public Customer create(CustomerDTO dto) {
        Customer customer = dto.fromDTO(null, em);
        Currency USD = currencies.findByCode("USD");
        CustomerAccount customerAccount = customerAccounts.create("Customer account for " + customer.getFullName(), new Money(USD, BigDecimal.ZERO));
        customer.setCustomerAccount(customerAccount);
        customerAccount.setCustomer(customer);
        em.persist(customer);
        return customer;
    }

    public boolean delete(Long id) {
        Customer entity = em.find(Customer.class, id);
        if (entity == null) {
            return false;
        }
        em.remove(entity);
        return true;
    }

    public Customer findById(Long id) {
        TypedQuery<Customer> findByIdQuery = em.createQuery("SELECT DISTINCT c FROM Customer c LEFT JOIN FETCH c.contacts LEFT JOIN FETCH c.customerAccount WHERE c.id = :entityId ORDER BY c.id", Customer.class);
        findByIdQuery.setParameter("entityId", id);
        Customer entity;
        try {
            entity = findByIdQuery.getSingleResult();
        } catch (NoResultException nre) {
            entity = null;
        }
        Account financialAccount = accounts.findByAccountId(entity.getCustomerAccount().getFinancialAccount().getAccountReference());
        entity.getCustomerAccount().setFinancialAccount(financialAccount);
        return entity;
    }

    public List<Customer> listAll(Integer startPosition, Integer maxResult, String email) {
        TypedQuery<Customer> findAllQuery = null;
        if (email == null || email.isEmpty()) {
            findAllQuery = em.createQuery("SELECT DISTINCT c FROM Customer c LEFT JOIN FETCH c.contacts LEFT JOIN FETCH c.customerAccount ORDER BY c.id", Customer.class);
        } else {
            findAllQuery = em.createQuery("SELECT DISTINCT c FROM Customer c LEFT JOIN FETCH c.contacts LEFT JOIN FETCH c.customerAccount WHERE LOWER(c.emailAddress) = :emailAddress ORDER BY c.id", Customer.class);
            findAllQuery.setParameter("emailAddress", email.toLowerCase());
        }
        if (startPosition != null) {
            findAllQuery.setFirstResult(startPosition);
        }
        if (maxResult != null) {
            findAllQuery.setMaxResults(maxResult);
        }
        final List<Customer> searchResults = findAllQuery.getResultList();

        for(Customer customer: searchResults) {
            Account financialAccount = accounts.findByAccountId(customer.getCustomerAccount().getFinancialAccount().getAccountReference());
            customer.getCustomerAccount().setFinancialAccount(financialAccount);
        }
        return searchResults;
    }

    public Customer update(CustomerDTO dto) {
        Customer entity = em.find(Customer.class, dto.getId());
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

    public Customer findByIBAN(String iban) {
        TypedQuery<Customer> findByIdQuery = em.createQuery("SELECT DISTINCT c FROM Customer c LEFT JOIN FETCH c.contacts LEFT JOIN c.customerAccount acc WHERE acc.iban = :iban ORDER BY c.id", Customer.class);
        findByIdQuery.setParameter("iban", iban);
        Customer entity;
        try {
            entity = findByIdQuery.getSingleResult();
        } catch (NoResultException nre) {
            entity = null;
        }
        return entity;
    }
}
