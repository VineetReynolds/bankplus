package org.jboss.examples.bankplus.services;

import org.jboss.examples.bankplus.model.accounting.Account;
import org.jboss.examples.bankplus.model.accounting.EntryType;
import org.jboss.examples.bankplus.model.accounting.JournalEntry;
import org.jboss.examples.bankplus.model.accounting.PostingStatus;
import org.jboss.examples.bankplus.model.customer.Customer;
import org.jboss.examples.bankplus.model.money.Currency;
import org.jboss.examples.bankplus.model.money.Money;
import org.jboss.examples.bankplus.model.transactions.Deposit;

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
        if (amount.compareTo(BigDecimal.ZERO) == -1) {
            throw new DepositException("Negative amount specified for deposit");
        }
        Deposit deposit = new Deposit();
        deposit.setDepositor(to);
        deposit.setDateTime(new Date());
        Currency USD = currencies.findByCode("USD");
        deposit.setDepositAmount(new Money(USD, amount));
        postJournalEntries(deposit);
        em.persist(deposit);
        // Can be moved off into a batch
        journal.postToLedger(deposit.getJournalEntries());
        return deposit;
    }

    private void postJournalEntries(Deposit deposit) {
        Account depositorAccount = deposit.getDepositor().getCustomerAccount();
        Account cashAccount = accounts.getCashAccount();
        if(cashAccount == null) {
            throw new DepositException("Failed to find a Cash account in the system.");
        }
        final JournalEntry creditEntry = new JournalEntry();
        creditEntry.setAccount(depositorAccount);
        creditEntry.setType(EntryType.CREDIT);
        creditEntry.setAmount(deposit.getDepositAmount());
        creditEntry.setPostingStatus(PostingStatus.UNPOSTED);
        final JournalEntry debitEntry = new JournalEntry();
        debitEntry.setType(EntryType.DEBIT);
        debitEntry.setAccount(cashAccount);
        debitEntry.setAmount(deposit.getDepositAmount());
        debitEntry.setPostingStatus(PostingStatus.UNPOSTED);
        final Set<JournalEntry> journalEntries = new HashSet<>();
        journalEntries.add(debitEntry);
        journalEntries.add(creditEntry);
        String description = "Deposit: (" + deposit.getDepositAmount() + ") to [" + deposit.getDepositor().getFullName() + "]";

        deposit.setDescription(description);
        deposit.setJournalEntries(journalEntries);
    }

}
