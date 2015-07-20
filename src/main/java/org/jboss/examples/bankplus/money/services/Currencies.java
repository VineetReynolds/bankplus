package org.jboss.examples.bankplus.money.services;

import org.jboss.examples.bankplus.money.model.Currency;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Stateless
public class Currencies {

    @PersistenceContext
    private EntityManager em;

    public Currency create(String name, String code) {
        Currency currency = new Currency();
        currency.setName(name);
        currency.setCurrencyCode(code);
        em.persist(currency);
        return currency;
    }

    public Currency findByCode(String code) {
        TypedQuery<Currency> findByCodeQuery = em.createQuery("SELECT DISTINCT c FROM Currency c WHERE c.currencyCode = :currencyCode ORDER BY c.id", Currency.class);
        findByCodeQuery.setParameter("currencyCode", code);
        Currency currency;
        try {
            currency = findByCodeQuery.getSingleResult();
        } catch (NoResultException nre) {
            currency = null;
        }
        return currency;
    }
}
