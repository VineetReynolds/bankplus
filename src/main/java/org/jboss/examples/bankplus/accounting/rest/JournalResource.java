package org.jboss.examples.bankplus.accounting.rest;

import org.jboss.examples.bankplus.accounting.model.JournalEntry;
import org.jboss.examples.bankplus.accounting.rest.representation.JournalEntryRepresentation;
import org.jboss.examples.bankplus.accounting.services.Accounts;
import org.jboss.examples.bankplus.accounting.services.Journal;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.Set;

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
    public Response create(Set<JournalEntryRepresentation> uploadedJournalEntries) {
        Set<JournalEntry> entries = JournalEntryRepresentation.from(uploadedJournalEntries, accounts);
        journal.postToLedger(entries);
        return Response.accepted()
                .build();
    }
}
