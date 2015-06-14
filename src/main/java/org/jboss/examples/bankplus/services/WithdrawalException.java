package org.jboss.examples.bankplus.services;

import javax.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class WithdrawalException extends RuntimeException {

    public WithdrawalException() {
    }

    public WithdrawalException(String message) {
        super(message);
    }

    public WithdrawalException(String message, Throwable cause) {
        super(message, cause);
    }

    public WithdrawalException(Throwable cause) {
        super(cause);
    }
}
