package org.jboss.examples.bankplus.customer.services;

import org.jboss.examples.bankplus.core.exception.BusinessException;

import javax.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class CustomerAccountException extends BusinessException {

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
