package org.jboss.examples.bankplus.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.jboss.examples.bankplus.model.customer.Customer;
import org.jboss.examples.bankplus.rest.dto.ContactDTO;
import org.jboss.examples.bankplus.model.customer.Contact;

/**
 *
 */
@Stateless
public class ContactResource {

    @Context
    private UriInfo uriInfo;

    @PersistenceContext(unitName = "Bankplus-persistence-unit")
    private EntityManager em;

    @POST
    @Consumes("application/json")
    public Response create(ContactDTO dto) {
        Contact entity = dto.fromDTO(null, em);
        Long customerId = Long.parseLong(uriInfo.getPathParameters().getFirst("id"));
        Customer customer = em.find(Customer.class, customerId);
        entity.setCustomer(customer);
        customer.getContacts().add(entity);
        em.merge(customer);
        em.flush();
        return Response.created(uriInfo.getAbsolutePathBuilder().path(String.valueOf(entity.getId())).build())
                .build();
    }

    @DELETE
    @Path("/{id:[0-9][0-9]*}")
    public Response deleteById(@PathParam("id") Long id) {
        Contact entity = em.find(Contact.class, id);
        if (entity == null) {
            return Response.status(Status.NOT_FOUND)
                    .build();
        }
        em.remove(entity);
        return Response.noContent()
                .build();
    }

    @GET
    @Path("/{id:[0-9][0-9]*}")
    @Produces("application/json")
    public Response findById(@PathParam("id") Long id) {
        TypedQuery<Contact> findByIdQuery = em.createQuery("SELECT DISTINCT c FROM Contact c WHERE c.id = :entityId ORDER BY c.id", Contact.class);
        findByIdQuery.setParameter("entityId", id);
        Contact entity;
        try {
            entity = findByIdQuery.getSingleResult();
        } catch (NoResultException nre) {
            entity = null;
        }
        if (entity == null) {
            return Response.status(Status.NOT_FOUND)
                    .build();
        }
        ContactDTO dto = new ContactDTO(entity);
        return Response.ok(dto)
                .build();
    }

    @GET
    @Produces("application/json")
    public Response listAll(@QueryParam("start") Integer startPosition, @QueryParam("max") Integer maxResult) {
        TypedQuery<Contact> findAllQuery = em.createQuery("SELECT DISTINCT c FROM Contact c WHERE c.customer = :customer ORDER BY c.id", Contact.class);
        if (startPosition != null) {
            findAllQuery.setFirstResult(startPosition);
        }
        if (maxResult != null) {
            findAllQuery.setMaxResults(maxResult);
        }
        Long customerId = Long.parseLong(uriInfo.getPathParameters().getFirst("id"));
        Customer customer = em.find(Customer.class, customerId);
        findAllQuery.setParameter("customer", customer);
        final List<Contact> searchResults = findAllQuery.getResultList();
        final List<ContactDTO> results = new ArrayList<ContactDTO>();
        for (Contact searchResult : searchResults) {
            ContactDTO dto = new ContactDTO(searchResult);
            results.add(dto);
        }
        return Response.ok(results)
                .build();
    }

    @PUT
    @Path("/{id:[0-9][0-9]*}")
    @Consumes("application/json")
    public Response update(@PathParam("id") Long id, ContactDTO dto) {
        if (dto == null) {
            return Response.status(Status.BAD_REQUEST)
                    .build();
        }
        if (!id.equals(dto.getId())) {
            return Response.status(Status.CONFLICT).entity(dto)
                    .build();
        }
        Contact entity = em.find(Contact.class, id);
        if (entity == null) {
            return Response.status(Status.NOT_FOUND)
                    .build();
        }
        entity = dto.fromDTO(entity, em);
        try {
            entity = em.merge(entity);
        } catch (OptimisticLockException e) {
            return Response.status(Status.CONFLICT).entity(e.getEntity())
                    .build();
        }
        ContactDTO updatedDTO = new ContactDTO(entity);
        return Response.ok(updatedDTO)
                .build();
    }
}
