package org.jboss.examples.bankplus.rest;

import org.jboss.examples.bankplus.model.customer.Contact;
import org.jboss.examples.bankplus.model.customer.Customer;
import org.jboss.examples.bankplus.model.transactions.Payment;
import org.jboss.examples.bankplus.rest.dto.PaymentDTO;
import org.jboss.examples.bankplus.services.PaymentService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Stateless
public class PaymentsResource {

    @Context
    private UriInfo uriInfo;

    @PersistenceContext(unitName = "Bankplus-persistence-unit")
    private EntityManager em;

    @Inject
    private PaymentService paymentService;

    @POST
    @Consumes("application/json")
    public Response create(PaymentDTO dto) {
        Long customerId = Long.parseLong(uriInfo.getPathParameters().getFirst("id"));
        Customer from = em.find(Customer.class, customerId);
        Contact to = em.find(Contact.class, dto.getPayeeId());
        Payment payment = paymentService.newOutgoingPayment(from, to, dto.getAmount());
        return Response.created(uriInfo.getAbsolutePathBuilder().path(String.valueOf(payment.getId())).build())
                .build();
    }
}
