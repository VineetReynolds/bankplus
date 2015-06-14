package org.jboss.examples.bankplus.services;

import javax.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class PaymentException extends RuntimeException {

    public PaymentException() {
    }

    public PaymentException(String message) {
        super(message);
    }

    public PaymentException(String message, Throwable cause) {
        super(message, cause);
    }

    public PaymentException(Throwable cause) {
        super(cause);
    }
}
