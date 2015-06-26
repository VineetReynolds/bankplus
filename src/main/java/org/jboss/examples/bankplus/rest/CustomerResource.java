package org.jboss.examples.bankplus.rest;

import org.jboss.examples.bankplus.model.customer.Customer;
import org.jboss.examples.bankplus.rest.dto.CustomerDTO;
import org.jboss.examples.bankplus.services.CustomerService;
import org.jboss.examples.bankplus.services.CustomerUpdateException;

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
    public Response create(CustomerDTO dto) {
        Customer customer = customerService.create(dto);
        return Response.created(UriBuilder.fromResource(CustomerResource.class).path(String.valueOf(customer.getId())).build())
                .entity(new CustomerDTO(customer))
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
        Customer customer = customerService.findById(id);
        if (customer == null) {
            return Response.status(Status.NOT_FOUND)
                    .build();
        }
        CustomerDTO dto = new CustomerDTO(customer);
        return Response.ok(dto)
                .build();
    }

    @GET
    @Produces("application/json")
    public Response listAll(@QueryParam("start") Integer startPosition, @QueryParam("max") Integer maxResult, @QueryParam("email") String email) {
        System.out.println("Email:" + email);
        List<Customer> searchResults = customerService.listAll(startPosition, maxResult, email);
        final List<CustomerDTO> results = new ArrayList<CustomerDTO>();
        for (Customer searchResult : searchResults) {
            CustomerDTO dto = new CustomerDTO(searchResult);
            results.add(dto);
        }
        return Response.ok(results)
                .build();
    }

    @PUT
    @Path("/{id:[0-9][0-9]*}")
    @Consumes("application/json")
    public Response update(@PathParam("id") Long id, CustomerDTO dto) {
        if (dto == null) {
            return Response.status(Status.BAD_REQUEST)
                    .build();
        }
        if (!id.equals(dto.getId())) {
            return Response.status(Status.CONFLICT).entity(dto)
                    .build();
        }
        try {
            Customer updatedCustomer = customerService.update(dto);
            if (updatedCustomer == null) {
                return Response.status(Status.NOT_FOUND)
                        .build();
            }
            CustomerDTO responseDto = new CustomerDTO(updatedCustomer);
            return Response.ok(responseDto)
                    .build();
        } catch (CustomerUpdateException e) {

            return Response.status(Status.CONFLICT).entity(e.getMessage())
                    .build();
        }
    }

    @Path("/{id:[0-9][0-9]*}/contacts")
    public ContactResource getContactResource(@PathParam("id") Long id) {
        Customer customer = customerService.findById(id);
        if (customer == null) {
            throw new WebApplicationException(Status.NOT_FOUND);
        }
        return contactResource;
    }

    @Path("/{id:[0-9][0-9]*}/deposits")
    public DepositsResource getDepositsResource(@PathParam("id") Long id) {
        Customer customer = customerService.findById(id);
        if (customer == null) {
            throw new WebApplicationException(Status.NOT_FOUND);
        }
        return depositsResource;
    }

    @Path("/{id:[0-9][0-9]*}/withdrawals")
    public WithdrawalsResource getWithdrawalsResource(@PathParam("id") Long id) {
        Customer customer = customerService.findById(id);
        if (customer == null) {
            throw new WebApplicationException(Status.NOT_FOUND);
        }
        return withdrawalsResource;
    }

    @Path("/{id:[0-9][0-9]*}/payments")
    public PaymentsResource getPaymentsResource(@PathParam("id") Long id) {
        Customer customer = customerService.findById(id);
        if (customer == null) {
            throw new WebApplicationException(Status.NOT_FOUND);
        }
        return paymentsResource;
    }

    @Path("/{id:[0-9][0-9]*}/reports")
    public ReportsResource getReportResource(@PathParam("id") Long id) {
        Customer customer = customerService.findById(id);
        if (customer == null) {
            throw new WebApplicationException(Status.NOT_FOUND);
        }
        return reportsResource;
    }

}
