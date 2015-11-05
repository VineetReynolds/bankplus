package org.jboss.examples.bankplus.reporting.rest;

import org.jboss.examples.bankplus.reporting.model.Customer;
import org.jboss.examples.bankplus.reporting.model.EntryType;
import org.jboss.examples.bankplus.core.rest.dto.DateWrapper;
import org.jboss.examples.bankplus.money.model.Money;
import org.jboss.examples.bankplus.reporting.model.JournalEntry;
import org.jboss.examples.bankplus.reporting.rest.dto.StatementLineItemDTO;
import org.jboss.examples.bankplus.reporting.services.client.*;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Path("/{id:[0-9][0-9]*}/reports")
@Stateless
public class ReportsResource {

    @Context
    private UriInfo uriInfo;

    @Inject
    private Accounts accounts;

    @Inject
    private Customers customers;

    @Inject
    private Journal journal;

    @Inject
    private AccountBalances accountBalances;

    @GET
    @Path("/monthly")
    @Produces("application/json")
    public Response getMonthlyReport(@QueryParam("start") Integer startPosition, @QueryParam("max") Integer maxResult) {
        Long customerId = Long.parseLong(uriInfo.getPathParameters().getFirst("id"));
        Customer customer = customers.findById(customerId);

        final List<JournalEntry> searchResults = journal.getEntriesForMonth(customer.getCustomerAccount().getFinancialAccount().getAccountReference());

        LocalDate start = LocalDate.now().withDayOfMonth(1);

        String accountId = customer.getCustomerAccount().getFinancialAccount().getAccountReference();
        AccountBalance accountBalance = accountBalances.getBalance(accountId, start);

        final List<StatementLineItemDTO> results = new ArrayList<>();
        Money runningBalance = accountBalance.getBalance();

        StatementLineItemDTO openingBalanceStatement = new StatementLineItemDTO();
        openingBalanceStatement.setDescription("Opening Balance");
        openingBalanceStatement.setBalance(runningBalance);
        results.add(openingBalanceStatement);

        for (JournalEntry searchResult : searchResults) {
            runningBalance = searchResult.getType() == EntryType.CREDIT ? runningBalance.add(searchResult.getAmount()) : runningBalance.subtract(searchResult.getAmount());
            StatementLineItemDTO dto = new StatementLineItemDTO(searchResult);
            dto.setBalance(runningBalance);
            results.add(dto);
        }
        return Response.ok(results)
                .build();
    }

    @GET
    @Path("/yearly")
    @Produces("application/json")
    public Response getYearlyReport(@QueryParam("start") Integer startPosition, @QueryParam("max") Integer maxResult) {
        Long customerId = Long.parseLong(uriInfo.getPathParameters().getFirst("id"));
        Customer customer = customers.findById(customerId);

        final List<JournalEntry> searchResults = journal.getEntriesForYear(customer.getCustomerAccount().getFinancialAccount().getAccountReference());

        LocalDate start = LocalDate.now().withDayOfMonth(1).withDayOfYear(1);

        String accountId = customer.getCustomerAccount().getFinancialAccount().getAccountReference();
        AccountBalance accountBalance = accountBalances.getBalance(accountId, start);

        final List<StatementLineItemDTO> results = new ArrayList<>();
        Money runningBalance = accountBalance.getBalance();

        StatementLineItemDTO openingBalanceStatement = new StatementLineItemDTO();
        openingBalanceStatement.setDescription("Opening Balance");
        openingBalanceStatement.setBalance(runningBalance);
        results.add(openingBalanceStatement);

        for (JournalEntry searchResult : searchResults) {
            runningBalance = searchResult.getType() == EntryType.CREDIT ? runningBalance.add(searchResult.getAmount()) : runningBalance.subtract(searchResult.getAmount());
            StatementLineItemDTO dto = new StatementLineItemDTO(searchResult);
            dto.setBalance(runningBalance);
            results.add(dto);
        }
        return Response.ok(results)
                .build();
    }

    @GET
    @Produces("application/json")
    public Response getCustomReport(@QueryParam("start") Integer startPosition, @QueryParam("max") Integer maxResult, @QueryParam("fromDate") DateWrapper wrappedFrom, @QueryParam("toDate") DateWrapper wrappedTo) {
        Long customerId = Long.parseLong(uriInfo.getPathParameters().getFirst("id"));
        Customer customer = customers.findById(customerId);

        LocalDate from = null;
        LocalDate to = null;
        if(wrappedFrom != null) {
            from = wrappedFrom.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }
        if(wrappedTo != null) {
            to = wrappedTo.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }

        final List<JournalEntry> searchResults = journal.getEntries(customer.getCustomerAccount().getFinancialAccount().getAccountReference(), from, to);

        String accountId = customer.getCustomerAccount().getFinancialAccount().getAccountReference();
        AccountBalance accountBalance = accountBalances.getBalance(accountId, from);
        LocalDate accountBalanceDate = accountBalance.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        if (from != null && from.isAfter(accountBalanceDate)) {
            throw new WebApplicationException("Enter a valid 'From' date.", Response.Status.NOT_FOUND);
        }
        else if (to != null && to.isBefore(accountBalanceDate)){
            throw new WebApplicationException("Enter a valid 'To' Date.", Response.Status.NOT_FOUND);
        }

        final List<StatementLineItemDTO> results = new ArrayList<>();
        Money runningBalance = accountBalance.getBalance();

        StatementLineItemDTO openingBalanceStatement = new StatementLineItemDTO();
        openingBalanceStatement.setDescription("Opening Balance");
        openingBalanceStatement.setBalance(runningBalance);
        results.add(openingBalanceStatement);

        for (JournalEntry searchResult : searchResults) {
            runningBalance = searchResult.getType() == EntryType.CREDIT ? runningBalance.add(searchResult.getAmount()) : runningBalance.subtract(searchResult.getAmount());
            StatementLineItemDTO dto = new StatementLineItemDTO(searchResult);
            dto.setBalance(runningBalance);
            results.add(dto);
        }
        return Response.ok(results)
                .build();
    }
}
