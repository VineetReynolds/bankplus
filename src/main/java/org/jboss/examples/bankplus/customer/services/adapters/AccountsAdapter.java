package org.jboss.examples.bankplus.customer.services.adapters;

import org.jboss.examples.bankplus.accounting.model.AccountType;
import org.jboss.examples.bankplus.customer.model.Account;
import org.jboss.examples.bankplus.customer.services.translators.AccountTranslator;
import org.jboss.examples.bankplus.money.model.Money;

import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

public class AccountsAdapter {

    @Inject
    private AccountTranslator translator;

    public org.jboss.examples.bankplus.customer.model.Account getLiabilitiesAccount() {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target("http://localhost:9080/bankplus/rest/").path("accounts")
                .queryParam("type", "liabilities");
        org.jboss.examples.bankplus.accounting.rest.representations.Account account = target.request(MediaType.APPLICATION_JSON_TYPE).get(org.jboss.examples.bankplus.accounting.rest.representations.Account.class);
        return translator.translate(account);
    }

    public Account newAccount(String accountId, String name, AccountType accountType, Account parentAccount, Money openingBalance) {
        org.jboss.examples.bankplus.accounting.rest.representations.Account newAccount = new org.jboss.examples.bankplus.accounting.rest.representations.Account();
        newAccount.setAccountId(accountId);
        newAccount.setName(name);
        newAccount.setAccountType(accountType);
        newAccount.setParentAccountId(parentAccount.getAccountReference());
        newAccount.setCurrentBalance(openingBalance);

        Client client = ClientBuilder.newClient();
        WebTarget target = client.target("http://localhost:9080/bankplus/rest/").path("accounts")
                .queryParam("type", "liabilities");
        org.jboss.examples.bankplus.accounting.rest.representations.Account account = target.request(MediaType.APPLICATION_JSON_TYPE).post(Entity.json(newAccount), org.jboss.examples.bankplus.accounting.rest.representations.Account.class);
        return translator.translate(account);
    }

    public Account findByAccountId(String accountId) {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target("http://localhost:9080/bankplus/rest/").path("accounts")
                .queryParam("accountId", accountId);
        org.jboss.examples.bankplus.accounting.rest.representations.Account account = target.request(MediaType.APPLICATION_JSON_TYPE).get(org.jboss.examples.bankplus.accounting.rest.representations.Account.class);
        return translator.translate(account);
    }
}
