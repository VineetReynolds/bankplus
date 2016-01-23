package org.jboss.examples.bankplus.transactions.services.adapters;

import org.jboss.examples.bankplus.transactions.services.client.OutgoingPaymentMessage;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

public class OutgoingPaymentsAdapter {

    private static final String host;

    private static final int port;

    static {
        String envHost = System.getenv("BANKPLUS_MESSAGING_SERVICE_HOST");
        host = envHost == null ? "bankplus_messaging.dev.docker" : envHost;
        String envPort = System.getenv("BANKPLUS_MESSAGING_SERVICE_PORT");
        port = envPort == null ? 8080 : Integer.parseInt(envPort);
    }


    public void publishMessage(OutgoingPaymentMessage paymentMessage) {
        Client client = ClientBuilder.newClient();
        UriBuilder builder = UriBuilder.fromUri("http://{host}:{port}/bankplus-messaging/rest/")
                .path("outgoingpayments")
                .resolveTemplate("host", host)
                .resolveTemplate("port", port);
        WebTarget target = client.target(builder);

        target.request(MediaType.APPLICATION_JSON_TYPE).post(Entity.json(paymentMessage));
    }
}
