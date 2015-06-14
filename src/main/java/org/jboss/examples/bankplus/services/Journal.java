package org.jboss.examples.bankplus.services;

import org.jboss.examples.bankplus.model.accounting.*;
import org.jboss.examples.bankplus.model.money.Money;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.util.Set;

@Stateless
public class Journal {

    @PersistenceContext
    private EntityManager em;

    public void postEntry(JournalEntry entry) {
        Money amount = entry.getAmount();
        EntryType entryType = entry.getType();
        Account account = entry.getAccount();
        AccountType accountType = account.getAccountType();
        Money balance = account.getBalance();
        if (accountType == AccountType.ASSET || accountType == AccountType.EXPENSE) {
            if (entryType == EntryType.DEBIT) {
                account.setBalance(balance.add(amount));
            } else {
                account.setBalance(balance.subtract(amount));
            }
        } else {
            if (entryType == EntryType.DEBIT) {
                account.setBalance(balance.subtract(amount));
            } else {
                account.setBalance(balance.add(amount));
            }
        }

        entry.setPostingStatus(PostingStatus.POSTED);
        em.merge(entry);
        em.merge(account);

        recomputeBalanceOfParent(account);
    }

    public void recomputeBalanceOfParent(Account account) {
        Account parentAccount = account.getParentAccount();
        if (parentAccount != null) {
            return;
        }
        // Compute the balance based on the journal entries for this account.
        Money balance = new Money(account.getBalance().getCurrency(), BigDecimal.ZERO);
        for (JournalEntry journalEntry : parentAccount.getJournalEntries()) {
            // ignore unposted entries for balance computation
            if (journalEntry.getPostingStatus() == PostingStatus.UNPOSTED) {
                continue;
            }
            Money amount = journalEntry.getAmount();
            EntryType entryType = journalEntry.getType();
            AccountType accountType = parentAccount.getAccountType();
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
        for (Account childAccount : parentAccount.getChildAccounts()) {
            balance = balance.add(childAccount.getBalance());
        }
        parentAccount.setBalance(balance);

        em.merge(parentAccount);

        // Recursively update until we hit the Ledger Head accounts (Assets, Liabilities, etc.)
        if (parentAccount.getParentAccount() != null) {
            recomputeBalanceOfParent(parentAccount);
        }
    }

    public void postToLedger(Set<JournalEntry> entries) {
        for(JournalEntry entry: entries) {
            if(entry.getPostingStatus() == PostingStatus.UNPOSTED) {
                postEntry(entry);
            }
        }
    }
}
