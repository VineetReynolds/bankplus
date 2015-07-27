package org.jboss.examples.bankplus.customer.services;

import org.jboss.examples.bankplus.customer.model.Contact;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Stateless
public class Contacts {

    @PersistenceContext
    private EntityManager em;


    public Contact findById(Long contactId) {
        TypedQuery<Contact> findByIdQuery = em.createQuery("SELECT DISTINCT c FROM Contact c WHERE c.id = :entityId ORDER BY c.id", Contact.class);
        findByIdQuery.setParameter("entityId", contactId);
        Contact entity;
        try {
            entity = findByIdQuery.getSingleResult();
        } catch (NoResultException nre) {
            entity = null;
        }
        return entity;
    }
}
