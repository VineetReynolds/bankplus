package org.jboss.examples.bankplus.reporting.services.adapters;

import org.jboss.examples.bankplus.reporting.services.client.AccountBalance;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class AccountBalanceAdapter {

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
        WebTarget target = client.target("http://localhost:9080/bankplus-accounting/rest/").path("accountbalances")
                .queryParam("accountId", accountId).queryParam("date", rangeStart);
        AccountBalance accountBalance = target.request(MediaType.APPLICATION_JSON_TYPE).get(AccountBalance.class);
        return accountBalance;
    }
}
