package org.jboss.examples.bankplus.customer.iban;

import org.apache.commons.validator.routines.checkdigit.CheckDigitException;

import java.util.Locale;

import static org.apache.commons.validator.routines.checkdigit.IBANCheckDigit.IBAN_CHECK_DIGIT;

public class USIban {

    public static final String DEFAULT_CHECK_DIGIT = "00";
    private final String value;

    private USIban(final String value) {
        StringBuffer sb = new StringBuffer();
        /* Convert the paper or display formatted value of IBAN numbers to the normal form */
        for(Character ch: value.toCharArray()) {
            if(Character.isLetterOrDigit(ch)) {
                sb.append(ch);
            }
        }
        this.value = sb.toString();
    }

    public String toFormattedString() {
        final StringBuilder ibanBuffer = new StringBuilder(value);
        final int length = ibanBuffer.length();

        for (int i = 0; i < length / 4; i++) {
            ibanBuffer.insert((i + 1) * 4 + i, ' ');
        }

        return ibanBuffer.toString().trim();
    }

    public boolean isValid() {
        return IBAN_CHECK_DIGIT.isValid(value);
    }

    public static final class Builder {

        private String bankCode;
        private String accountNumber;

        public Builder bankCode(final String bankCode) {
            this.bankCode = bankCode;
            return this;
        }

        public Builder accountNumber(final String accountNumber) {
            this.accountNumber = accountNumber;
            return this;
        }

        public USIban build() {
            StringBuilder sb = new StringBuilder();
            sb.append(Locale.US.getCountry());
            sb.append(DEFAULT_CHECK_DIGIT);
            sb.append(bankCode);
            sb.append(accountNumber);
            String iban = sb.toString();
            try {
                return new USIban(iban.substring(0,2) + IBAN_CHECK_DIGIT.calculate(iban) + iban.substring(4));
            } catch (CheckDigitException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
