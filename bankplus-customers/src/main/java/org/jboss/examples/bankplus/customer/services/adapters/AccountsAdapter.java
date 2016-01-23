package org.jboss.examples.bankplus.customer.services.adapters;

import org.jboss.examples.bankplus.customer.model.Account;
import org.jboss.examples.bankplus.customer.services.client.AccountType;
import org.jboss.examples.bankplus.customer.services.translators.AccountTranslator;
import org.jboss.examples.bankplus.money.model.Money;

import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

public class AccountsAdapter {

    @Inject
    private AccountTranslator translator;

    private static final String host;

    private static final int port;

    static {
        String envHost = System.getenv("BANKPLUS_ACCOUNTING_SERVICE_HOST");
        host = envHost == null ? "bankplus_accounting.dev.docker" : envHost;
        String envPort = System.getenv("BANKPLUS_ACCOUNTING_SERVICE_PORT");
        port = envPort == null ? 8080 : Integer.parseInt(envPort);
    }

    public org.jboss.examples.bankplus.customer.model.Account getLiabilitiesAccount() {
        Client client = ClientBuilder.newClient();
        UriBuilder builder = UriBuilder.fromUri("http://{host}:{port}/bankplus-accounting/rest/")
                .path("accounts")
                .queryParam("type", "liabilities")
                .resolveTemplate("host", host)
                .resolveTemplate("port", port);
        WebTarget target = client.target(builder);
        org.jboss.examples.bankplus.customer.services.adapters.Account account = target.request(MediaType.APPLICATION_JSON_TYPE).get(org.jboss.examples.bankplus.customer.services.adapters.Account.class);
        return translator.translate(account);
    }

    public Account newAccount(String accountId, String name, AccountType accountType, Account parentAccount, Money openingBalance) {
        org.jboss.examples.bankplus.customer.services.adapters.Account newAccount = new org.jboss.examples.bankplus.customer.services.adapters.Account();
        newAccount.setAccountId(accountId);
        newAccount.setName(name);
        newAccount.setAccountType(accountType);
        newAccount.setParentAccountId(parentAccount.getAccountReference());
        newAccount.setCurrentBalance(openingBalance);

        Client client = ClientBuilder.newClient();
        UriBuilder builder = UriBuilder.fromUri("http://{host}:{port}/bankplus-accounting/rest/")
                .path("accounts")
                .queryParam("type", "liabilities")
                .resolveTemplate("host", host)
                .resolveTemplate("port", port);
        WebTarget target = client.target(builder);
        org.jboss.examples.bankplus.customer.services.adapters.Account account = target.request(MediaType.APPLICATION_JSON_TYPE).post(Entity.json(newAccount), org.jboss.examples.bankplus.customer.services.adapters.Account.class);
        return translator.translate(account);
    }

    public Account findByAccountId(String accountId) {
        Client client = ClientBuilder.newClient();
        UriBuilder builder = UriBuilder.fromUri("http://{host}:{port}/bankplus-accounting/rest/")
                .path("accounts")
                .queryParam("accountId", accountId)
                .resolveTemplate("host", host)
                .resolveTemplate("port", port);
        WebTarget target = client.target(builder);
        org.jboss.examples.bankplus.customer.services.adapters.Account account = target.request(MediaType.APPLICATION_JSON_TYPE).get(org.jboss.examples.bankplus.customer.services.adapters.Account.class);
        return translator.translate(account);
    }
}
