package org.jboss.examples.bankplus.transactions.services;

import org.jboss.examples.bankplus.accounting.model.EntryType;
import org.jboss.examples.bankplus.money.model.Currency;
import org.jboss.examples.bankplus.money.model.Money;
import org.jboss.examples.bankplus.money.services.Currencies;
import org.jboss.examples.bankplus.transactions.model.Account;
import org.jboss.examples.bankplus.transactions.model.Customer;
import org.jboss.examples.bankplus.transactions.model.Deposit;
import org.jboss.examples.bankplus.transactions.model.JournalEntry;
import org.jboss.examples.bankplus.transactions.services.client.Accounts;
import org.jboss.examples.bankplus.transactions.services.client.Journal;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Stateless
public class DepositService {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private Currencies currencies;

    @Inject
    private Accounts accounts;

    @Inject
    private Journal journal;

    public Deposit newDeposit(Customer to, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new DepositException("A positive amount should be specified for deposits.");
        }
        Deposit deposit = new Deposit();
        deposit.setDepositor(to);
        deposit.setDateTime(new Date());
        Currency USD = currencies.findByCode("USD");
        deposit.setDepositAmount(new Money(USD, amount));
        deposit.setDescription("Deposit: " + deposit.getDepositAmount().getCurrency().getCurrencyCode() + " " + deposit.getDepositAmount().getAmount()
                + " to account by " + deposit.getDepositor().getFullName());
        em.persist(deposit);

        Set<JournalEntry> journalEntries = prepareJournalEntries(deposit);

        journal.recordJournalEntries(journalEntries);
        return deposit;
    }

    private Set<JournalEntry> prepareJournalEntries(Deposit deposit) {
        Account depositorAccount = deposit.getDepositor().getCustomerAccount().getFinancialAccount();
        Account cashAccount = accounts.getCashAccount();
        if (cashAccount == null) {
            throw new DepositException("Failed to find a Cash account in the system.");
        }
        final JournalEntry creditEntry = new JournalEntry();
        creditEntry.setAccount(depositorAccount);
        creditEntry.setType(EntryType.CREDIT);
        creditEntry.setAmount(deposit.getDepositAmount());
        creditEntry.setDateTime(deposit.getDateTime());
        creditEntry.setEventReference(deposit.getId());
        creditEntry.setDescription(deposit.getDescription());
        final JournalEntry debitEntry = new JournalEntry();
        debitEntry.setType(EntryType.DEBIT);
        debitEntry.setAccount(cashAccount);
        debitEntry.setAmount(deposit.getDepositAmount());
        debitEntry.setDateTime(deposit.getDateTime());
        debitEntry.setEventReference(deposit.getId());
        debitEntry.setDescription(deposit.getDescription());
        final Set<JournalEntry> journalEntries = new HashSet<>();
        journalEntries.add(debitEntry);
        journalEntries.add(creditEntry);
        return journalEntries;
    }

}
