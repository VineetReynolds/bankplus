package org.jboss.examples.bankplus.accounting.rest;

import org.jboss.examples.bankplus.accounting.rest.representations.Account;
import org.jboss.examples.bankplus.accounting.services.Accounts;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.util.ArrayList;
import java.util.List;

@Path("/accounts")
@Stateless
public class AccountsResource {

    @Inject
    private Accounts accounts;

    @GET
    @Produces("application/json")
    public Response getAccount(@QueryParam("type") String type, @QueryParam("accountId") String accountId) {
        if(type != null && !type.isEmpty()) {
            org.jboss.examples.bankplus.accounting.model.Account account = null;
            switch (type) {
                case "cash":
                    account = accounts.getCashAccount();
                    break;
                case "charges":
                    account = accounts.getChargesAccount();
                    break;
                case "clearing":
                    account = accounts.getClearingAccount();
                    break;
                case "liabilities":
                    account = accounts.getLiabilitiesAccount();
                    break;
                default:
                    throw new IllegalArgumentException("Invalid account type");
            }
            Account accountRepresentation = new Account(account);
            return Response.ok(accountRepresentation).build();
        } else if (accountId != null && !accountId.isEmpty()) {
            org.jboss.examples.bankplus.accounting.model.Account result = accounts.findByAccountId(accountId);
            Account accountRepresentation = new Account(result);
            return Response.ok(accountRepresentation).build();
        } else {
            List<org.jboss.examples.bankplus.accounting.model.Account> allAccounts = this.accounts.listAll();
            List<Account> accounts = new ArrayList<>();
            for(org.jboss.examples.bankplus.accounting.model.Account account: allAccounts) {
                Account accountRepresentation = new Account(account);
                accounts.add(accountRepresentation);
            }
            return Response.ok(accounts).build();
        }
    }

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Response createAccount(Account account) {
        org.jboss.examples.bankplus.accounting.model.Account parentAccount = accounts.findByAccountId(account.getParentAccountId());
        org.jboss.examples.bankplus.accounting.model.Account newAccount = accounts.newAccount(account.getAccountId(), account.getName(), account.getAccountType(), parentAccount, account.getCurrentBalance());
        return Response.created(UriBuilder.fromResource(AccountsResource.class).path(String.valueOf(newAccount.getId())).build())
                .entity(new Account(newAccount))
                .build();
    }

}
