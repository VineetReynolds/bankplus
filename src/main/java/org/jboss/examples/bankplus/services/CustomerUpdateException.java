package org.jboss.examples.bankplus.services;

import javax.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class CustomerUpdateException extends RuntimeException {

    public CustomerUpdateException() {
        super();
    }

    public CustomerUpdateException(String message) {
        super(message);
    }

    public CustomerUpdateException(String message, Throwable cause) {
        super(message, cause);
    }

    public CustomerUpdateException(Throwable cause) {
        super(cause);
    }
}
