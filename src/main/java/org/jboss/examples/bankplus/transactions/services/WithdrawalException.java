package org.jboss.examples.bankplus.transactions.services;

import org.jboss.examples.bankplus.core.exception.BusinessException;

import javax.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class WithdrawalException extends BusinessException {

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
