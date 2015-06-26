package org.jboss.examples.bankplus.rest.dto;

import org.jboss.examples.bankplus.model.accounting.EntryType;
import org.jboss.examples.bankplus.model.accounting.JournalEntry;
import org.jboss.examples.bankplus.model.transactions.Deposit;
import org.jboss.examples.bankplus.model.transactions.Event;
import org.jboss.examples.bankplus.model.transactions.Payment;
import org.jboss.examples.bankplus.model.transactions.Withdrawal;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class StatementLineItemDTO implements Serializable {

    private Long id;

    private Date dateTime;

    private String description;

    private String type;

    private BigDecimal amount;

    public StatementLineItemDTO() {

    }

    public StatementLineItemDTO(JournalEntry entity) {
        if (entity != null) {
            this.id = entity.getId();
            this.dateTime = entity.getDateTime();
            this.description = entity.getFinancialEvent().getDescription();
            this.type = entity.getType() == EntryType.CREDIT ? "Deposit" : "Withdrawal";
            this.amount = entity.getAmount().getAmount();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
