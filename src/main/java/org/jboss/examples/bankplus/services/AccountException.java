package org.jboss.examples.bankplus.services;

import javax.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class AccountException extends RuntimeException {

    public AccountException() {
        super();
    }

    public AccountException(String message) {
        super(message);
    }

    public AccountException(String message, Throwable cause) {
        super(message, cause);
    }

    public AccountException(Throwable cause) {
        super(cause);
    }
}
