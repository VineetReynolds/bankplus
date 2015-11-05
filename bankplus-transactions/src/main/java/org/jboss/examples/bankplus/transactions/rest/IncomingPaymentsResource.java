package org.jboss.examples.bankplus.transactions.rest;

import org.jboss.examples.bankplus.transactions.model.Contact;
import org.jboss.examples.bankplus.transactions.model.Customer;
import org.jboss.examples.bankplus.transactions.rest.representation.IncomingPayment;
import org.jboss.examples.bankplus.transactions.rest.representation.Payment;
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
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.logging.Logger;

@Path("/incomingpayments")
@Stateless
public class IncomingPaymentsResource {

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
    public Response create(IncomingPayment dto) {
        Long customerId = Long.parseLong(uriInfo.getPathParameters().getFirst("id"));
        String iban = dto.getBeneficiary();
        Customer to = customers.findByIBAN(iban);
        if(to == null) {
            // TODO Post the payment message to a failed message queue.
            // TODO How do institutions handle this?
            Logger.getLogger(IncomingPaymentsResource.class.getName()).severe("Failed to find customer account");
            throw new PaymentException("Beneficiary customer was not found.");
        }
        String payer = dto.getOrderingCustomer();
        org.jboss.examples.bankplus.transactions.model.IncomingPayment payment = paymentService.newIncomingPayment(to, payer, dto.getAmount());
        return Response.created(uriInfo.getAbsolutePathBuilder().path(String.valueOf(payment.getId())).build())
                .build();
    }
}
