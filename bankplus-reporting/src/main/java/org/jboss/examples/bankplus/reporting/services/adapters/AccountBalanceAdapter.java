package org.jboss.examples.bankplus.reporting.services.adapters;

import org.jboss.examples.bankplus.reporting.services.client.AccountBalance;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class AccountBalanceAdapter {

    private static final String host;

    private static final int port;

    static {
        String envHost = System.getenv("BANKPLUS_ACCOUNTING_SERVICE_HOST");
        host = envHost == null ? "bankplus_accounting.dev.docker" : envHost;
        String envPort = System.getenv("BANKPLUS_ACCOUNTING_SERVICE_PORT");
        port = envPort == null ? 8080 : Integer.parseInt(envPort);
    }

    public AccountBalance getBalance(String accountId, LocalDate from) {
        String rangeStart = null;
        if(from != null) {
            Instant start = from.atStartOfDay()
                    .toInstant(ZoneOffset.UTC);
            rangeStart = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX")
                    .withZone(ZoneOffset.UTC)
                    .format(start);
        }

        Client client = ClientBuilder.newClient();
        UriBuilder builder = UriBuilder.fromUri("http://{host}:{port}/bankplus-accounting/rest/")
                .path("accountbalances")
                .queryParam("accountId", accountId)
                .queryParam("date", rangeStart)
                .resolveTemplate("host", host)
                .resolveTemplate("port", port);
        WebTarget target = client.target(builder);
        AccountBalance accountBalance = target.request(MediaType.APPLICATION_JSON_TYPE).get(AccountBalance.class);
        return accountBalance;
    }
}
