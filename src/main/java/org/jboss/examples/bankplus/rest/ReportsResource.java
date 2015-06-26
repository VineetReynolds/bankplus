package org.jboss.examples.bankplus.rest;

import org.jboss.examples.bankplus.model.accounting.JournalEntry;
import org.jboss.examples.bankplus.model.customer.Customer;
import org.jboss.examples.bankplus.rest.dto.StatementLineItemDTO;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Stateless
public class ReportsResource {

    @Context
    private UriInfo uriInfo;

    @PersistenceContext(unitName = "Bankplus-persistence-unit")
    private EntityManager em;

    @GET
    @Path("/monthly")
    @Produces("application/json")
    public Response getMonthlyReport(@QueryParam("start") Integer startPosition, @QueryParam("max") Integer maxResult) {
        TypedQuery<JournalEntry> findAllQuery = em.createQuery("SELECT DISTINCT j FROM JournalEntry j LEFT JOIN FETCH j.financialEvent WHERE j.account = :accountId ORDER BY j.dateTime", JournalEntry.class);
        if (startPosition != null) {
            findAllQuery.setFirstResult(startPosition);
        }
        if (maxResult != null) {
            findAllQuery.setMaxResults(maxResult);
        }
        Long customerId = Long.parseLong(uriInfo.getPathParameters().getFirst("id"));
        Customer customer = em.find(Customer.class, customerId);
        findAllQuery.setParameter("accountId", customer.getCustomerAccount());
        final List<JournalEntry> searchResults = findAllQuery.getResultList();
        final List<StatementLineItemDTO> results = new ArrayList<>();
        for (JournalEntry searchResult : searchResults) {
            StatementLineItemDTO dto = new StatementLineItemDTO(searchResult);
            results.add(dto);
        }
        return Response.ok(results)
                .build();
    }

    @GET
    @Path("/yearly")
    @Produces("application/json")
    public Response getYearlyReport(@QueryParam("start") Integer startPosition, @QueryParam("max") Integer maxResult) {
        TypedQuery<JournalEntry> findAllQuery = em.createQuery("SELECT DISTINCT j FROM JournalEntry j WHERE j.account = :accountId ORDER BY j.dateTime", JournalEntry.class);
        if (startPosition != null) {
            findAllQuery.setFirstResult(startPosition);
        }
        if (maxResult != null) {
            findAllQuery.setMaxResults(maxResult);
        }
        Long customerId = Long.parseLong(uriInfo.getPathParameters().getFirst("id"));
        Customer customer = em.find(Customer.class, customerId);
        findAllQuery.setParameter("accountId", customer.getCustomerAccount());
        final List<JournalEntry> searchResults = findAllQuery.getResultList();
        final List<StatementLineItemDTO> results = new ArrayList<>();
        for (JournalEntry searchResult : searchResults) {
            StatementLineItemDTO dto = new StatementLineItemDTO(searchResult);
            results.add(dto);
        }
        return Response.ok(results)
                .build();
    }

    @GET
    @Produces("application/json")
    public Response getCustomReport(@QueryParam("start") Integer startPosition, @QueryParam("max") Integer maxResult, @QueryParam("from") Date from, @QueryParam("to") Date to) {
        TypedQuery<JournalEntry> findAllQuery = em.createQuery("SELECT DISTINCT j FROM JournalEntry j WHERE j.account = :accountId ORDER BY j.dateTime", JournalEntry.class);
        if (startPosition != null) {
            findAllQuery.setFirstResult(startPosition);
        }
        if (maxResult != null) {
            findAllQuery.setMaxResults(maxResult);
        }
        Long customerId = Long.parseLong(uriInfo.getPathParameters().getFirst("id"));
        Customer customer = em.find(Customer.class, customerId);
        findAllQuery.setParameter("accountId", customer.getCustomerAccount());
        final List<JournalEntry> searchResults = findAllQuery.getResultList();
        final List<StatementLineItemDTO> results = new ArrayList<>();
        for (JournalEntry searchResult : searchResults) {
            StatementLineItemDTO dto = new StatementLineItemDTO(searchResult);
            results.add(dto);
        }
        return Response.ok(results)
                .build();
    }
}
