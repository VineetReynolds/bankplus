package org.jboss.examples.bankplus.accounting.rest;

import org.jboss.examples.bankplus.accounting.rest.representations.JournalEntry;
import org.jboss.examples.bankplus.accounting.services.Accounts;
import org.jboss.examples.bankplus.accounting.services.Journal;
import org.jboss.examples.bankplus.core.rest.dto.DateWrapper;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.*;

@Stateless
@Path("/journal")
public class JournalResource {

    @Inject
    private Journal journal;

    @Inject
    private Accounts accounts;

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Response create(Set<JournalEntry> uploadedJournalEntries) {
        Set<org.jboss.examples.bankplus.accounting.model.JournalEntry> entries = JournalEntry.from(uploadedJournalEntries, accounts);
        journal.postToLedger(entries);
        return Response.accepted()
                .build();
    }

    @GET
    @Produces("application/json")
    public Response get(@QueryParam("accountId") String accountId, @QueryParam("start") DateWrapper start, @QueryParam("end") DateWrapper end) {
        Date startDate = null;
        if(start != null && start.getDate() != null) {
            startDate = start.getDate();
        }
        Date endDate = null;
        if(end != null && end.getDate() != null) {
            endDate = end.getDate();
        }
        Collection<org.jboss.examples.bankplus.accounting.model.JournalEntry> entries = journal.getEntries(accountId, startDate, endDate);

        List<JournalEntry> journalEntries = new ArrayList<>();
        for(org.jboss.examples.bankplus.accounting.model.JournalEntry entry: entries) {
            JournalEntry journalEntry = new JournalEntry(entry);
            journalEntries.add(journalEntry);
        }
        return Response.ok(journalEntries).build();
    }
}
