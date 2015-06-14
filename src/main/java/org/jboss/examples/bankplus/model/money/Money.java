package org.jboss.examples.bankplus.model.money;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Embeddable
public class Money {

    public Money() {
    }

    @NotNull
    @ManyToOne
    private Currency currency;

    @NotNull
    private BigDecimal amount;

    public Money(Currency currency, BigDecimal amount) {
        this.currency = currency;
        this.amount = amount;
    }

    public Money add(final Money augend) {
        if (augend.currency.equals(currency)) {
            BigDecimal sum = this.amount.add(augend.amount);
            return new Money(currency, sum);
        } else {
            throw new IllegalArgumentException("Currencies must match.");
        }
    }

    public Money subtract(Money subtrahend) {
        if (subtrahend.currency.equals(currency)) {
            BigDecimal difference = this.amount.subtract(subtrahend.amount);
            return new Money(currency, difference);
        } else {
            throw new IllegalArgumentException("Currencies must match.");
        }
    }

    public Money multiply(Money multiplicand) {
        if (multiplicand.currency.equals(currency)) {
            BigDecimal sum = this.amount.multiply(multiplicand.amount);
            return new Money(currency, sum);
        } else {
            throw new IllegalArgumentException("Currencies must match.");
        }
    }

    public Money divide(Money divisor) {
        if (divisor.currency.equals(currency)) {
            BigDecimal sum = this.amount.divide(divisor.amount);
            return new Money(currency, sum);
        } else {
            throw new IllegalArgumentException("Currencies must match.");
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((amount == null) ? 0 : amount.hashCode());
        result = prime * result + ((currency == null) ? 0 : currency.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Money)) {
            return false;
        }
        Money other = (Money) obj;
        return (currency.equals(other.currency) && (amount.compareTo(other.amount) == 0));
    }

    @Override
    public String toString() {
        return "Money [" + currency + ", " + amount + "]";
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Currency getCurrency() {
        return currency;
    }
}
