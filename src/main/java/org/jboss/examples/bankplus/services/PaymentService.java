package org.jboss.examples.bankplus.services;

import org.jboss.examples.bankplus.model.accounting.Account;
import org.jboss.examples.bankplus.model.accounting.EntryType;
import org.jboss.examples.bankplus.model.accounting.JournalEntry;
import org.jboss.examples.bankplus.model.accounting.PostingStatus;
import org.jboss.examples.bankplus.model.customer.Contact;
import org.jboss.examples.bankplus.model.customer.Customer;
import org.jboss.examples.bankplus.model.messages.OutgoingPaymentMessage;
import org.jboss.examples.bankplus.model.money.Currency;
import org.jboss.examples.bankplus.model.money.Money;
import org.jboss.examples.bankplus.model.transactions.Payment;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Stateless
public class PaymentService {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private Currencies currencies;

    @Inject
    private Accounts accounts;

    @Inject
    private CustomerAccounts customerAccounts;

    @Inject
    private Journal journal;

    public Payment newPayment(Customer from, Contact to, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) == -1) {
            throw new DepositException("Negative amount specified for withdrawal");
        }
        Payment payment = new Payment();
        payment.setPayer(from);
        payment.setPayee(to);
        payment.setDateTime(new Date());
        Currency USD = currencies.findByCode("USD");
        payment.setPaymentAmount(new Money(USD, amount));
        postJournalEntries(payment);
        em.persist(payment);
        // Can be moved off into a batch
        journal.postToLedger(payment.getJournalEntries());
        return payment;
    }

    private void postJournalEntries(Payment payment) {
        Money amount = payment.getPaymentAmount();
        Account payeeAccount = customerAccounts.findByIBAN(payment.getPayee().getIban());
        if(payeeAccount == null) {
            Account clearingAccount = accounts.getClearingAccount();
            if(clearingAccount == null) {
                throw new PaymentException("Failed to find a clearing house for the contact.");
            } else {
                payeeAccount = clearingAccount;
                generatePaymentMessage(payment);
            }
        }
        Currency USD = currencies.findByCode("USD");
        Money charges = new Money(USD, amount.getAmount().multiply(new BigDecimal("0.005")));
        Money totalTransactionAmount = amount.add(charges);
        if(payment.getPayer().getCustomerAccount().getBalance().getAmount().compareTo(totalTransactionAmount.getAmount()) == -1) {
            throw new PaymentException("Insufficient balance in the account");
        }
        final JournalEntry creditEntry = new JournalEntry();
        creditEntry.setAccount(payeeAccount);
        creditEntry.setType(EntryType.CREDIT);
        creditEntry.setAmount(amount);
        creditEntry.setPostingStatus(PostingStatus.UNPOSTED);
        final JournalEntry debitEntry = new JournalEntry();
        debitEntry.setType(EntryType.DEBIT);
        debitEntry.setAccount(payment.getPayer().getCustomerAccount());
        debitEntry.setAmount(totalTransactionAmount);
        debitEntry.setPostingStatus(PostingStatus.UNPOSTED);
        final JournalEntry chargesEntry = new JournalEntry();
        chargesEntry.setType(EntryType.CREDIT);
        chargesEntry.setAccount(accounts.getChargesAccount());
        chargesEntry.setAmount(charges);
        chargesEntry.setPostingStatus(PostingStatus.UNPOSTED);
        final Set<JournalEntry> journalEntries = new HashSet<>();
        journalEntries.add(debitEntry);
        journalEntries.add(creditEntry);
        journalEntries.add(chargesEntry);

        String description = "Payment: [" + payment.getPayer().getFullName() + "] -> [" + payment.getPayee().getFullName() + "] (" + payment.getPaymentAmount() + ")";

        payment.setDescription(description);
        payment.setJournalEntries(journalEntries);
    }

    public void generatePaymentMessage(Payment payment) {
        OutgoingPaymentMessage outgoingPaymentMessage = new OutgoingPaymentMessage();
        outgoingPaymentMessage.setOrderingCustomer(payment.getPayer().getFullName());
        outgoingPaymentMessage.setBeneficiary(payment.getPayee().getIban());
        outgoingPaymentMessage.setAmount(payment.getPaymentAmount());
        outgoingPaymentMessage.setBookingDate(payment.getDateTime());
        outgoingPaymentMessage.setProcessed(false);
        outgoingPaymentMessage.generate();
        payment.setOutgoingPaymentMessage(outgoingPaymentMessage);
        em.persist(outgoingPaymentMessage);
    }

}
