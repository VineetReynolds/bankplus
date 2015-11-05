package org.jboss.examples.bankplus.transactions.services.adapters;

import org.jboss.examples.bankplus.transactions.services.client.Account;
import org.jboss.examples.bankplus.transactions.services.translators.AccountTranslator;

import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

public class AccountsAdapter {

    @Inject
    private AccountTranslator translator;

    public org.jboss.examples.bankplus.transactions.model.Account getCashAccount() {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target("http://localhost:9080/bankplus-accounting/rest/").path("accounts")
                .queryParam("type", "cash");
        Account account = target.request(MediaType.APPLICATION_JSON_TYPE).get(Account.class);
        return translator.translate(account);
    }

    public org.jboss.examples.bankplus.transactions.model.Account getChargesAccount() {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target("http://localhost:9080/bankplus-accounting/rest/").path("accounts")
                .queryParam("type", "charges");
        Account account = target.request(MediaType.APPLICATION_JSON_TYPE).get(Account.class);
        return translator.translate(account);
    }

    public org.jboss.examples.bankplus.transactions.model.Account getClearingAccount() {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target("http://localhost:9080/bankplus-accounting/rest/").path("accounts")
                .queryParam("type", "clearing");
        Account account = target.request(MediaType.APPLICATION_JSON_TYPE).get(Account.class);
        return translator.translate(account);
    }
}
