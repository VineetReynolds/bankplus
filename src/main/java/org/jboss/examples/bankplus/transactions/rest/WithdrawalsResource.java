package org.jboss.examples.bankplus.transactions.rest;

import org.jboss.examples.bankplus.customer.model.Customer;
import org.jboss.examples.bankplus.transactions.model.Withdrawal;
import org.jboss.examples.bankplus.transactions.rest.dto.WithdrawalDTO;
import org.jboss.examples.bankplus.transactions.services.WithdrawalService;

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
public class WithdrawalsResource {

    @Context
    private UriInfo uriInfo;

    @PersistenceContext(unitName = "Bankplus-persistence-unit")
    private EntityManager em;

    @Inject
    private WithdrawalService withdrawalService;

    @POST
    @Consumes("application/json")
    public Response create(WithdrawalDTO dto) {
        Long customerId = Long.parseLong(uriInfo.getPathParameters().getFirst("id"));
        Customer customer = em.find(Customer.class, customerId);
        Withdrawal withdrawal = withdrawalService.newWithdrawal(customer, dto.getAmount());
        return Response.created(uriInfo.getAbsolutePathBuilder().path(String.valueOf(withdrawal.getId())).build())
                .build();
    }
}