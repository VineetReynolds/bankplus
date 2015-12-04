package org.jboss.examples.bankplus.messages.services.adapters;

import org.jboss.examples.bankplus.messages.services.client.IncomingPayment;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

public class IncomingPaymentsAdapter {

    private static final String host;

    private static final int port;

    static {
        String envHost = System.getenv("TRANSACTIONS_PORT_8080_TCP_ADDR");
        host = envHost == null ? "bankplus_transactions.dev.docker" : envHost;
        String envPort = System.getenv("TRANSACTIONS_PORT_8080_TCP_PORT");
        port = envPort == null ? 8080 : Integer.parseInt(envPort);
    }

    public void newIncomingPayment(IncomingPayment paymentMessage) {
        Client client = ClientBuilder.newClient();
        UriBuilder builder = UriBuilder.fromUri("http://{host}:{port}/bankplus-transactions/rest/")
                .path("incomingpayments")
                .resolveTemplate("host", host)
                .resolveTemplate("port", port);
        WebTarget target = client.target(builder);

        target.request(MediaType.APPLICATION_JSON_TYPE).post(Entity.json(paymentMessage));
    }
}
