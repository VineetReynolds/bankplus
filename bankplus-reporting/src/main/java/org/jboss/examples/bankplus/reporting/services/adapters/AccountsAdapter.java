package org.jboss.examples.bankplus.reporting.services.adapters;

import org.jboss.examples.bankplus.reporting.services.client.Account;
import org.jboss.examples.bankplus.reporting.services.translators.AccountTranslator;

import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
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


    public org.jboss.examples.bankplus.reporting.model.Account getCashAccount() {
        Client client = ClientBuilder.newClient();
        UriBuilder builder = UriBuilder.fromUri("http://{host}:{port}/bankplus-accounting/rest/")
                .path("accounts")
                .queryParam("type", "cash")
                .resolveTemplate("host", host)
                .resolveTemplate("port", port);
        WebTarget target = client.target(builder);
        Account account = target.request(MediaType.APPLICATION_JSON_TYPE).get(Account.class);
        return translator.translate(account);
    }

    public org.jboss.examples.bankplus.reporting.model.Account getChargesAccount() {
        Client client = ClientBuilder.newClient();
        UriBuilder builder = UriBuilder.fromUri("http://{host}:{port}/bankplus-accounting/rest/")
                .path("accounts")
                .queryParam("type", "charges")
                .resolveTemplate("host", host)
                .resolveTemplate("port", port);
        WebTarget target = client.target(builder);
        Account account = target.request(MediaType.APPLICATION_JSON_TYPE).get(Account.class);
        return translator.translate(account);
    }

    public org.jboss.examples.bankplus.reporting.model.Account getClearingAccount() {
        Client client = ClientBuilder.newClient();
        UriBuilder builder = UriBuilder.fromUri("http://{host}:{port}/bankplus-accounting/rest/")
                .path("accounts")
                .queryParam("type", "clearing")
                .resolveTemplate("host", host)
                .resolveTemplate("port", port);
        WebTarget target = client.target(builder);
        Account account = target.request(MediaType.APPLICATION_JSON_TYPE).get(Account.class);
        return translator.translate(account);
    }
}
