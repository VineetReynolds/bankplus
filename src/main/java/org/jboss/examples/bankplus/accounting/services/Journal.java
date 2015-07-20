package org.jboss.examples.bankplus.accounting.services;

import org.jboss.examples.bankplus.accounting.model.*;
import org.jboss.examples.bankplus.money.model.Money;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.*;

@Stateless
public class Journal {

    @PersistenceContext
    private EntityManager em;

    public void computeAccountBalance(Account account) {
        // Compute the balance based on the opening balance and the journal entries posted for this account.
        Money balance = account.getOpeningBalance();
        for (JournalEntry journalEntry : getPostedEntriesForAccountSince(account, account.getPeriodOpenDate())) {
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
        account.setLastUpdatedOn(new Date());

        em.merge(account);

        // Recursively update until we hit the Ledger Head accounts (Assets, Liabilities, etc.)
        Account parentAccount = account.getParentAccount();
        if (parentAccount != null) {
            computeAccountBalance(parentAccount);
        }
    }

    public void postToLedger(Set<JournalEntry> entries) {
        for(JournalEntry entry: entries) {
            if(entry.getPostingStatus() == PostingStatus.UNPOSTED) {
                entry.setPostingStatus(PostingStatus.POSTED);
                em.persist(entry);
                computeAccountBalance(entry.getAccount());
            }
        }
    }

    private Collection<JournalEntry> getPostedEntriesForAccountSince(Account account, Date dateTime) {
        String query = "SELECT j FROM JournalEntry j WHERE j.account = :account " +
                "AND j.postingStatus = org.jboss.examples.bankplus.accounting.model.PostingStatus.POSTED";
        TypedQuery<JournalEntry> journalEntryQuery = null;
        if(dateTime == null) {
            journalEntryQuery = em.createQuery(query, JournalEntry.class);
            journalEntryQuery.setParameter("account", account);
        } else {
            query += " AND j.dateTime >= :dateTime";
            journalEntryQuery = em.createQuery(query, JournalEntry.class);
            journalEntryQuery.setParameter("account", account);
            journalEntryQuery.setParameter("dateTime", dateTime);
        }
        List<JournalEntry> entries = journalEntryQuery.getResultList();
        return entries;
    }
}
