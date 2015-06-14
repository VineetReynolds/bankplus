package org.jboss.examples.bankplus.services;

import org.iban4j.CountryCode;
import org.iban4j.Iban;
import org.jboss.examples.bankplus.model.customer.Customer;
import org.jboss.examples.bankplus.model.customer.CustomerAccount;
import org.jboss.examples.bankplus.model.money.Currency;
import org.jboss.examples.bankplus.model.money.Money;
import org.jboss.examples.bankplus.rest.dto.CustomerDTO;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Stateless
public class CustomerService {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private Currencies currencies;

    @Inject
    private CustomerAccounts customerAccounts;

    public Customer create(CustomerDTO dto) {
        Customer customer = dto.fromDTO(null, em);
        Currency USD = currencies.findByCode("USD");
        CustomerAccount customerAccount = customerAccounts.create(null, "Customer account for " + customer.getFullName(), null);
        customer.setCustomerAccount(customerAccount);
        customerAccount.setCustomer(customer);
        em.persist(customer);
        Long id = customer.getCustomerAccount().getId();
        String accountId = String.format("2%010d", id);
        String iban = new Iban.Builder()
                .countryCode(CountryCode.AT)
                .bankCode("19043")
                .accountNumber(accountId)
                .build()
                .toFormattedString();
        customer.getCustomerAccount().setAccountId(accountId);
        customer.getCustomerAccount().setIban(iban);
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

}
