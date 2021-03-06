package org.jboss.examples.bankplus.customer.services;

import org.jboss.examples.bankplus.core.exception.BusinessException;

import javax.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class CustomerUpdateException extends BusinessException {

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
