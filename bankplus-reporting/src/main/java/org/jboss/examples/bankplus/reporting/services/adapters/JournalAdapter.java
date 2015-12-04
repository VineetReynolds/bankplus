package org.jboss.examples.bankplus.reporting.services.adapters;

import org.jboss.examples.bankplus.reporting.services.translators.JournalEntryTranslator;

import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class JournalAdapter {

    @Inject
    private JournalEntryTranslator translator;

    private static final String host;

    private static final int port;

    static {
        String envHost = System.getenv("ACCOUNTING_PORT_8080_TCP_ADDR");
        host = envHost == null ? "bankplus_accounting.dev.docker" : envHost;
        String envPort = System.getenv("ACCOUNTING_PORT_8080_TCP_PORT");
        port = envPort == null ? 8080 : Integer.parseInt(envPort);
    }

    public List<org.jboss.examples.bankplus.reporting.model.JournalEntry> getEntriesForMonth(String accountId) {
        Instant start = LocalDate.now()
                .withDayOfMonth(1)
                .atStartOfDay()
                .toInstant(ZoneOffset.UTC);
        String monthStart = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX")
                .withZone(ZoneOffset.UTC)
                .format(start);

        Client client = ClientBuilder.newClient();
        UriBuilder builder = UriBuilder.fromUri("http://{host}:{port}/bankplus-accounting/rest/")
                .path("journal")
                .queryParam("accountId", accountId)
                .queryParam("start", monthStart)
                .resolveTemplate("host", host)
                .resolveTemplate("port", port);
        WebTarget target = client.target(builder);

        GenericType<List<JournalEntry>> journalEntryType = new GenericType<List<JournalEntry>>() {};
        List<JournalEntry> entries = target.request(MediaType.APPLICATION_JSON_TYPE).get(journalEntryType);
        List<org.jboss.examples.bankplus.reporting.model.JournalEntry> journalEntries = entries.stream().map(translator::translate).collect(Collectors.toList());
        return journalEntries;
    }

    public List<org.jboss.examples.bankplus.reporting.model.JournalEntry> getEntriesForYear(String accountId) {
        Instant start = LocalDate.now()
                .withDayOfMonth(1)
                .withDayOfYear(1)
                .atStartOfDay()
                .toInstant(ZoneOffset.UTC);
        String yearStart = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX")
                .withZone(ZoneOffset.UTC)
                .format(start);

        Client client = ClientBuilder.newClient();
        UriBuilder builder = UriBuilder.fromUri("http://{host}:{port}/bankplus-accounting/rest/")
                .path("journal")
                .queryParam("accountId", accountId)
                .queryParam("start", yearStart)
                .resolveTemplate("host", host)
                .resolveTemplate("port", port);
        WebTarget target = client.target(builder);

        GenericType<List<JournalEntry>> journalEntryType = new GenericType<List<JournalEntry>>() {};
        List<JournalEntry> entries = target.request(MediaType.APPLICATION_JSON_TYPE).get(journalEntryType);
        List<org.jboss.examples.bankplus.reporting.model.JournalEntry> journalEntries = entries.stream().map(translator::translate).collect(Collectors.toList());
        return journalEntries;
    }

    public List<org.jboss.examples.bankplus.reporting.model.JournalEntry> getEntries(String accountId, LocalDate from, LocalDate to) {
        String rangeStart = null;
        String rangeEnd = null;
        if(from != null) {
            Instant start = from.atStartOfDay()
                    .toInstant(ZoneOffset.UTC);
            rangeStart = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX")
                    .withZone(ZoneOffset.UTC)
                    .format(start);
        }
        if(to != null) {
            Instant end = to
                    .atStartOfDay()
                    .toInstant(ZoneOffset.UTC);
            rangeEnd = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX")
                    .withZone(ZoneOffset.UTC)
                    .format(end);
        }

        Client client = ClientBuilder.newClient();
        UriBuilder builder = UriBuilder.fromUri("http://{host}:{port}/bankplus-accounting/rest/")
                .path("journal")
                .queryParam("accountId", accountId)
                .queryParam("start", rangeStart)
                .queryParam("end", rangeEnd)
                .resolveTemplate("host", host)
                .resolveTemplate("port", port);
        WebTarget target = client.target(builder);

        GenericType<List<JournalEntry>> journalEntryType = new GenericType<List<JournalEntry>>() {};
        List<JournalEntry> entries = target.request(MediaType.APPLICATION_JSON_TYPE).get(journalEntryType);
        List<org.jboss.examples.bankplus.reporting.model.JournalEntry> journalEntries = entries.stream().map(translator::translate).collect(Collectors.toList());
        return journalEntries;
    }
}
