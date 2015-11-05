package org.jboss.examples.bankplus.messages.rest;

import org.jboss.examples.bankplus.messages.model.OutgoingPaymentMessage;
import org.jboss.examples.bankplus.messages.rest.representation.OutgoingPayment;
import org.jboss.examples.bankplus.messages.services.OutgoingPaymentProcessor;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

/**
 *
 */
@Stateless
@Path("/outgoingpayments")
public class OutgoingPaymentsResource {

    @PersistenceContext(unitName = "Bankplus-persistence-unit")
    private EntityManager em;

    @Context
    private HttpServletRequest httpRequest;

    @Inject
    private OutgoingPaymentProcessor outgoingPaymentProcessor;

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Response create(OutgoingPayment dto) {
        OutgoingPaymentMessage outgoingPaymentMessage = dto.fromDTO(null, em);
        outgoingPaymentMessage = outgoingPaymentProcessor.generateMessage(outgoingPaymentMessage);
        return Response.created(UriBuilder.fromResource(OutgoingPaymentsResource.class).path(String.valueOf(outgoingPaymentMessage.getId())).build())
                .build();
    }

}
