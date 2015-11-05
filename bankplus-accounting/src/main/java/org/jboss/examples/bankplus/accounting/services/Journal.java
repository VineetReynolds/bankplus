package org.jboss.examples.bankplus.accounting.services;

import org.jboss.examples.bankplus.accounting.model.*;
import org.jboss.examples.bankplus.money.model.Money;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.time.Instant;
import java.time.ZoneId;
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
            if(entry.getPostingStatus() == null || entry.getPostingStatus() == PostingStatus.UNPOSTED) {
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

    public void recordJournalEntries(Set<JournalEntry> entries) {
        for(JournalEntry entry: entries) {
            if(entry.getPostingStatus() == null) {
                entry.setPostingStatus(PostingStatus.UNPOSTED);
            }
            em.persist(entry);
        }
        postToLedger(entries);
    }

    public Collection<JournalEntry> getEntries(String accountId, Date start, Date end) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<JournalEntry> q = cb.createQuery(JournalEntry.class);
        Root<JournalEntry> j = q.from(JournalEntry.class);
        ParameterExpression<String> accountParam = cb.parameter(String.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(j.get("account").get("accountId"), accountParam));
        if(start != null) {
            javax.persistence.criteria.Path<Date> dateTime = j.get("dateTime");
            predicates.add(cb.greaterThanOrEqualTo(dateTime, start));
        }
        if(end != null) {
            javax.persistence.criteria.Path<Date> dateTime = j.get("dateTime");
            // Adjust the date by one more day, as date comparison will result
            // in ignoring transactions occurring after 12:00 AM.
            Date nextDay = Date.from(Instant.from(end.toInstant().atZone(ZoneId.systemDefault()).plusDays(1)));
            predicates.add(cb.lessThanOrEqualTo(dateTime, nextDay));
        }
        q.select(j)
                .where(predicates.toArray(new Predicate[]{}))
                .orderBy(cb.asc(j.get("id")), cb.asc(j.get("dateTime")));

        TypedQuery<JournalEntry> findAllQuery = em.createQuery(q);
        findAllQuery.setParameter(accountParam, accountId);
        final List<JournalEntry> searchResults = findAllQuery.getResultList();
        return searchResults;
    }
}
