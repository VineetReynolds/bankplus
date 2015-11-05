package org.jboss.examples.bankplus.messages.services.client;

import org.jboss.examples.bankplus.messages.services.adapters.IncomingPaymentsAdapter;

import javax.inject.Inject;

/**
 * Created by vreynolds on 10/30/15.
 */
public class IncomingPayments {

    @Inject
    private IncomingPaymentsAdapter paymentsAdapter;

    public void newIncomingPayment(IncomingPayment paymentMessage) {
        paymentsAdapter.newIncomingPayment(paymentMessage);
    }
}
