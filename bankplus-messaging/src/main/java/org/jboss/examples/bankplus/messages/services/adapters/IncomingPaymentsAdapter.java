package org.jboss.examples.bankplus.messages.services.adapters;

import org.jboss.examples.bankplus.messages.services.client.IncomingPayment;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

public class IncomingPaymentsAdapter {
    public void newIncomingPayment(IncomingPayment paymentMessage) {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target("http://localhost:9080/bankplus-transactions/rest/").path("incomingpayments");

        target.request(MediaType.APPLICATION_JSON_TYPE).post(Entity.json(paymentMessage));
    }
}
