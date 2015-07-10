package org.jboss.examples.bankplus.services;

import org.jboss.examples.bankplus.model.accounting.Account;
import org.jboss.examples.bankplus.model.accounting.EntryType;
import org.jboss.examples.bankplus.model.accounting.JournalEntry;
import org.jboss.examples.bankplus.model.accounting.PostingStatus;
import org.jboss.examples.bankplus.model.customer.Customer;
import org.jboss.examples.bankplus.model.money.Currency;
import org.jboss.examples.bankplus.model.money.Money;
import org.jboss.examples.bankplus.model.transactions.Deposit;
import org.jboss.examples.bankplus.model.transactions.Withdrawal;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Stateless
public class WithdrawalService {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private Currencies currencies;

    @Inject
    private Accounts accounts;

    @Inject
    private Journal journal;

    public Withdrawal newWithdrawal(Customer from, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) == -1) {
            throw new WithdrawalException("Negative amount specified for withdrawal");
        }
        Withdrawal withdrawal = new Withdrawal();
        withdrawal.setWithdrawer(from);
        withdrawal.setDateTime(new Date());
        Currency USD = currencies.findByCode("USD");
        withdrawal.setWithdrawalAmount(new Money(USD, amount));
        postJournalEntries(withdrawal);
        em.persist(withdrawal);
        // Can be moved off into a batch
        journal.postToLedger(withdrawal.getJournalEntries());
        return withdrawal;
    }

    private void postJournalEntries(Withdrawal withdrawal) {
        Account withdrawalAccount = withdrawal.getWithdrawer().getCustomerAccount();
        Account cashAccount = accounts.getCashAccount();
        if(cashAccount == null) {
            throw new WithdrawalException("Failed to find a Cash account in the system.");
        }
        final JournalEntry creditEntry = new JournalEntry();
        creditEntry.setAccount(cashAccount);
        creditEntry.setType(EntryType.CREDIT);
        creditEntry.setAmount(withdrawal.getWithdrawalAmount());
        creditEntry.setDateTime(withdrawal.getDateTime());
        creditEntry.setFinancialEvent(withdrawal);
        creditEntry.setPostingStatus(PostingStatus.UNPOSTED);
        final JournalEntry debitEntry = new JournalEntry();
        debitEntry.setType(EntryType.DEBIT);
        debitEntry.setAccount(withdrawalAccount);
        debitEntry.setAmount(withdrawal.getWithdrawalAmount());
        debitEntry.setDateTime(withdrawal.getDateTime());
        debitEntry.setFinancialEvent(withdrawal);
        debitEntry.setPostingStatus(PostingStatus.UNPOSTED);
        final Set<JournalEntry> journalEntries = new HashSet<>();
        journalEntries.add(debitEntry);
        journalEntries.add(creditEntry);

        String description = "Withdrawal: " + withdrawal.getWithdrawalAmount().getCurrency().getCurrencyCode() + " " +  withdrawal.getWithdrawalAmount().getAmount()
                + " to account by " + withdrawal.getWithdrawer().getFullName();

        withdrawal.setDescription(description);
        withdrawal.setJournalEntries(journalEntries);
    }

}
