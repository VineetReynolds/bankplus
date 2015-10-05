package org.jboss.examples.bankplus.customer.rest;

import org.jboss.examples.bankplus.customer.rest.representations.Customer;
import org.jboss.examples.bankplus.reporting.rest.ReportsResource;
import org.jboss.examples.bankplus.customer.services.CustomerService;
import org.jboss.examples.bankplus.customer.services.CustomerUpdateException;
import org.jboss.examples.bankplus.transactions.rest.DepositsResource;
import org.jboss.examples.bankplus.transactions.rest.PaymentsResource;
import org.jboss.examples.bankplus.transactions.rest.WithdrawalsResource;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@Stateless
@Path("/customers")
public class CustomerResource {

    @PersistenceContext(unitName = "Bankplus-persistence-unit")
    private EntityManager em;

    @Inject
    private ContactResource contactResource;

    @Inject
    private DepositsResource depositsResource;

    @Inject
    private WithdrawalsResource withdrawalsResource;

    @Inject
    private PaymentsResource paymentsResource;

    @Inject
    private ReportsResource reportsResource;

    @Inject
    private CustomerService customerService;

    @Context
    private HttpServletRequest httpRequest;

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Response create(Customer dto) {
        org.jboss.examples.bankplus.customer.model.Customer customer = customerService.create(dto);
        return Response.created(UriBuilder.fromResource(CustomerResource.class).path(String.valueOf(customer.getId())).build())
                .entity(new Customer(customer))
                .build();
    }

    @DELETE
    @Path("/{id:[0-9][0-9]*}")
    public Response deleteById(@PathParam("id") Long id) {
        boolean deleted = customerService.delete(id);
        if (deleted) {
            return Response.noContent()
                    .build();
        } else {
            return Response.status(Status.NOT_FOUND)
                    .build();
        }
    }

    @GET
    @Path("/{id:[0-9][0-9]*}")
    @Produces("application/json")
    public Response findById(@PathParam("id") Long id) {
        org.jboss.examples.bankplus.customer.model.Customer customer = customerService.findById(id);
        if (customer == null) {
            return Response.status(Status.NOT_FOUND)
                    .build();
        }
        Customer dto = new Customer(customer);
        return Response.ok(dto)
                .build();
    }

    @GET
    @Produces("application/json")
    public Response listAll(@QueryParam("start") Integer startPosition, @QueryParam("max") Integer maxResult,  @QueryParam("iban") String iban, @QueryParam("email") String email) {
        if (iban == null || iban.isEmpty()) {
            List<org.jboss.examples.bankplus.customer.model.Customer> searchResults = customerService.listAll(startPosition, maxResult, email);
            final List<Customer> results = new ArrayList<Customer>();
            for (org.jboss.examples.bankplus.customer.model.Customer searchResult : searchResults) {
                Customer dto = new Customer(searchResult);
                results.add(dto);
            }
            return Response.ok(results)
                    .build();
        } else {
            org.jboss.examples.bankplus.customer.model.Customer match = customerService.findByIBAN(iban);
            Customer dto = new Customer(match);
            return Response.ok(dto).build();
        }

    }

    @PUT
    @Path("/{id:[0-9][0-9]*}")
    @Consumes("application/json")
    public Response update(@PathParam("id") Long id, Customer dto) {
        if (dto == null) {
            return Response.status(Status.BAD_REQUEST)
                    .build();
        }
        if (!id.equals(dto.getId())) {
            return Response.status(Status.CONFLICT).entity(dto)
                    .build();
        }
        try {
            org.jboss.examples.bankplus.customer.model.Customer updatedCustomer = customerService.update(dto);
            if (updatedCustomer == null) {
                return Response.status(Status.NOT_FOUND)
                        .build();
            }
            Customer responseDto = new Customer(updatedCustomer);
            return Response.ok(responseDto)
                    .build();
        } catch (CustomerUpdateException e) {

            return Response.status(Status.CONFLICT).entity(e.getMessage())
                    .build();
        }
    }

    @Path("/{id:[0-9][0-9]*}/contacts")
    public ContactResource getContactResource(@PathParam("id") Long id) {
        org.jboss.examples.bankplus.customer.model.Customer customer = customerService.findById(id);
        if (customer == null) {
            throw new WebApplicationException(Status.NOT_FOUND);
        }
        return contactResource;
    }

    @Path("/{id:[0-9][0-9]*}/deposits")
    public DepositsResource getDepositsResource(@PathParam("id") Long id) {
        org.jboss.examples.bankplus.customer.model.Customer customer = customerService.findById(id);
        if (customer == null) {
            throw new WebApplicationException(Status.NOT_FOUND);
        }
        return depositsResource;
    }

    @Path("/{id:[0-9][0-9]*}/withdrawals")
    public WithdrawalsResource getWithdrawalsResource(@PathParam("id") Long id) {
        org.jboss.examples.bankplus.customer.model.Customer customer = customerService.findById(id);
        if (customer == null) {
            throw new WebApplicationException(Status.NOT_FOUND);
        }
        return withdrawalsResource;
    }

    @Path("/{id:[0-9][0-9]*}/payments")
    public PaymentsResource getPaymentsResource(@PathParam("id") Long id) {
        org.jboss.examples.bankplus.customer.model.Customer customer = customerService.findById(id);
        if (customer == null) {
            throw new WebApplicationException(Status.NOT_FOUND);
        }
        return paymentsResource;
    }

    @Path("/{id:[0-9][0-9]*}/reports")
    public ReportsResource getReportResource(@PathParam("id") Long id) {
        org.jboss.examples.bankplus.customer.model.Customer customer = customerService.findById(id);
        if (customer == null) {
            throw new WebApplicationException(Status.NOT_FOUND);
        }
        return reportsResource;
    }

}
