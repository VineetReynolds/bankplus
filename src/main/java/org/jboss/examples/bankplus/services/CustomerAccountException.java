package org.jboss.examples.bankplus.services;

import javax.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class CustomerAccountException extends RuntimeException {

    public CustomerAccountException() {
        super();
    }

    public CustomerAccountException(String message) {
        super(message);
    }

    public CustomerAccountException(String message, Throwable cause) {
        super(message, cause);
    }

    public CustomerAccountException(Throwable cause) {
        super(cause);
    }
}
