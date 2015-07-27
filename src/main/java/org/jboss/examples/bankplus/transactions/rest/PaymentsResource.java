package org.jboss.examples.bankplus.transactions.rest;

import org.jboss.examples.bankplus.transactions.model.Contact;
import org.jboss.examples.bankplus.transactions.model.Customer;
import org.jboss.examples.bankplus.transactions.model.Payment;
import org.jboss.examples.bankplus.transactions.rest.dto.PaymentDTO;
import org.jboss.examples.bankplus.transactions.services.PaymentException;
import org.jboss.examples.bankplus.transactions.services.PaymentService;
import org.jboss.examples.bankplus.transactions.services.client.Contacts;
import org.jboss.examples.bankplus.transactions.services.client.Customers;

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

    @Inject
    private Customers customers;

    @Inject
    private Contacts contacts;

    @POST
    @Consumes("application/json")
    public Response create(PaymentDTO dto) {
        Long customerId = Long.parseLong(uriInfo.getPathParameters().getFirst("id"));
        Customer from = customers.findById(customerId);
        Long payeeId = dto.getPayeeId();
        if(payeeId == null) {
            throw new PaymentException("Contact was not specified.");
        }
        Contact to = contacts.findById(payeeId);
        if(to == null) {
            throw new PaymentException("Contact was not specified.");
        }
        Payment payment = paymentService.newOutgoingPayment(from, to, dto.getAmount());
        return Response.created(uriInfo.getAbsolutePathBuilder().path(String.valueOf(payment.getId())).build())
                .build();
    }
}
