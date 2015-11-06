package org.jboss.examples.bankplus.transactions.services.adapters;

import org.jboss.examples.bankplus.transactions.services.client.OutgoingPaymentMessage;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

public class OutgoingPaymentsAdapter {

    public void publishMessage(OutgoingPaymentMessage paymentMessage) {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target("http://localhost:11080/bankplus-messaging/rest/").path("outgoingpayments");

        target.request(MediaType.APPLICATION_JSON_TYPE).post(Entity.json(paymentMessage));
    }
}
