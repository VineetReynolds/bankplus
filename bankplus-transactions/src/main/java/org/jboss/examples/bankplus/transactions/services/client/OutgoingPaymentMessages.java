package org.jboss.examples.bankplus.transactions.services.client;

import org.jboss.examples.bankplus.transactions.services.adapters.OutgoingPaymentsAdapter;

import javax.inject.Inject;

public class OutgoingPaymentMessages {

    @Inject
    private OutgoingPaymentsAdapter adapter;

    public void publishMessage(OutgoingPaymentMessage paymentMessage) {
        adapter.publishMessage(paymentMessage);
    }
}
