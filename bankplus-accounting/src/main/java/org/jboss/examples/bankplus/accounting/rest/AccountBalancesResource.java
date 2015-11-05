package org.jboss.examples.bankplus.accounting.rest;

import org.jboss.examples.bankplus.accounting.model.AccountBalanceHistory;
import org.jboss.examples.bankplus.accounting.rest.representations.Account;
import org.jboss.examples.bankplus.accounting.rest.representations.AccountBalance;
import org.jboss.examples.bankplus.accounting.services.AccountBalances;
import org.jboss.examples.bankplus.accounting.services.Accounts;
import org.jboss.examples.bankplus.core.rest.dto.DateWrapper;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Path("/accountbalances")
@Stateless
public class AccountBalancesResource {

    @Inject
    private AccountBalances accountBalances;

    @GET
    @Produces("application/json")
    public Response getAccountBalance(@QueryParam("accountId") String accountId, @QueryParam("date") DateWrapper date) {
        Date from = null;
        if(date != null) {
            from = date.getDate();
        }
        AccountBalanceHistory balance = accountBalances.getBalance(accountId, from);
        AccountBalance balanceRepresentation = new AccountBalance(balance);
        return Response.ok(balanceRepresentation).build();
    }

}
