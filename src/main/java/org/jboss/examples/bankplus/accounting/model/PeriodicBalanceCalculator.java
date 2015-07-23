package org.jboss.examples.bankplus.accounting.model;

import org.jboss.examples.bankplus.accounting.services.Accounts;
import org.jboss.examples.bankplus.customer.model.CustomerAccount;
import org.jboss.examples.bankplus.money.model.Money;
import org.jboss.examples.bankplus.customer.services.CustomerAccounts;

import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Stateless
public class PeriodicBalanceCalculator {

    @Inject
    private Accounts accounts;

    @PersistenceContext
    private EntityManager em;

    @Schedule(minute = "*/1", hour = "*")
    public void scheduleBalanceComputation(){
        List<Account> allLeafAccounts = accounts.listLeafAccounts();
        for(Account leafAccount: allLeafAccounts) {
            updateAccountBalance(leafAccount);
        }
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void updateAccountBalance(Account account) {
        LocalDate previousDate = LocalDate.now().minusDays(1);
        TypedQuery<AccountBalanceHistory> yesterdayBalanceQuery = em.createQuery("SELECT DISTINCT bal FROM AccountBalanceHistory bal WHERE bal.account = :account AND bal.date = :previousDate", AccountBalanceHistory.class);
        yesterdayBalanceQuery.setParameter("account", account);
        yesterdayBalanceQuery.setParameter("previousDate", Date.from(previousDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        Money balance = null;
        try {
            AccountBalanceHistory previousBalance = yesterdayBalanceQuery.getSingleResult();
            balance = previousBalance.getOpeningBalance();
        } catch (NoResultException noRes) {
            balance = account.getOpeningBalance();
            Instant instant = Instant.ofEpochMilli(account.getPeriodOpenDate().getTime());
            previousDate = LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalDate();
        }

        // Compute the balance based on yesterday's opening balance
        // and the journal entries posted for this account for yesterday.
        // Use the opening balance if yesterday's balance is not found, as the account may have been opened yesterday.
        for (JournalEntry journalEntry : getPostedEntriesForAccountSince(account, previousDate, LocalDate.now())) {
            Money amount = journalEntry.getAmount();
            EntryType entryType = journalEntry.getType();
            AccountType accountType = account.getAccountType();
            if (accountType == AccountType.ASSET || accountType == AccountType.EXPENSE) {
                if (entryType == EntryType.DEBIT) {
                    balance = balance.add(amount);
                } else {
                    balance = balance.subtract(amount);
                }
            } else {
                if (entryType == EntryType.DEBIT) {
                    balance = balance.subtract(amount);
                } else {
                    balance = balance.add(amount);
                }
            }
        }

        // Assuming child account balances are up to date, recompute the parent's balance
        for (Account childAccount : account.getChildAccounts()) {
            balance = balance.add(childAccount.getCurrentBalance());
        }
        account.setCurrentBalance(balance);
        AccountBalanceHistory currentBalance = new AccountBalanceHistory();
        currentBalance.setOpeningBalance(balance);
        currentBalance.setAccount(account);
        Date today = Date.from(LocalDate.now().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        currentBalance.setDate(today);
        account.getBalanceHistories().add(currentBalance);
        account.setLastUpdatedOn(new Date());

        em.merge(account);

        // Recursively update until we hit the Ledger Head accounts (Assets, Liabilities, etc.)
        Account parentAccount = account.getParentAccount();
        if (parentAccount != null) {
            updateAccountBalance(parentAccount);
        }
    }

    private Collection<JournalEntry> getPostedEntriesForAccountSince(Account account, LocalDate from, LocalDate to) {
        String query = "SELECT j FROM JournalEntry j WHERE j.account = :account " +
                "AND j.postingStatus = org.jboss.examples.bankplus.accounting.model.PostingStatus.POSTED";
        TypedQuery<JournalEntry> journalEntryQuery = null;
        if(from == null) {
            journalEntryQuery = em.createQuery(query, JournalEntry.class);
            journalEntryQuery.setParameter("account", account);
        } else {
            query += " AND j.dateTime >= :from AND j.dateTime < :to";
            journalEntryQuery = em.createQuery(query, JournalEntry.class);
            journalEntryQuery.setParameter("account", account);
            journalEntryQuery.setParameter("from", Date.from(from.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
            journalEntryQuery.setParameter("to", Date.from(to.plusDays(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
        }
        List<JournalEntry> entries = journalEntryQuery.getResultList();
        return entries;
    }
}
