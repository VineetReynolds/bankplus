package org.jboss.examples.bankplus.transactions.rest;

import org.jboss.examples.bankplus.transactions.model.Customer;
import org.jboss.examples.bankplus.transactions.rest.representation.Deposit;
import org.jboss.examples.bankplus.transactions.services.DepositService;
import org.jboss.examples.bankplus.transactions.services.client.Customers;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path("/{id:[0-9][0-9]*}/deposits")
@Stateless
public class DepositsResource {

    @Context
    private UriInfo uriInfo;

    @Inject
    private Customers customers;

    @Inject
    private DepositService depositService;

    @POST
    @Consumes("application/json")
    public Response create(Deposit dto) {
        Long customerId = Long.parseLong(uriInfo.getPathParameters().getFirst("id"));
        Customer customer = customers.findById(customerId);
        org.jboss.examples.bankplus.transactions.model.Deposit deposit = depositService.newDeposit(customer, dto.getAmount());
        return Response.created(uriInfo.getAbsolutePathBuilder().path(String.valueOf(deposit.getId())).build())
                .build();
    }
}
