package org.jboss.examples.bankplus.messages.services;

import org.jboss.examples.bankplus.messages.model.OutgoingPaymentMessage;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class OutgoingPaymentProcessor {

    @PersistenceContext
    private EntityManager em;

    public OutgoingPaymentMessage generateMessage(OutgoingPaymentMessage outgoingPaymentMessage) {
        outgoingPaymentMessage.generate();
        em.persist(outgoingPaymentMessage);
        return outgoingPaymentMessage;
    }
}
