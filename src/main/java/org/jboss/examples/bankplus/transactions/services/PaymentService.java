package org.jboss.examples.bankplus.transactions.services;

import org.jboss.examples.bankplus.accounting.model.EntryType;
import org.jboss.examples.bankplus.messages.model.IncomingPaymentMessage;
import org.jboss.examples.bankplus.messages.model.OutgoingPaymentMessage;
import org.jboss.examples.bankplus.money.model.Currency;
import org.jboss.examples.bankplus.money.model.Money;
import org.jboss.examples.bankplus.money.services.Currencies;
import org.jboss.examples.bankplus.transactions.model.*;
import org.jboss.examples.bankplus.transactions.services.client.Accounts;
import org.jboss.examples.bankplus.transactions.services.client.Customers;
import org.jboss.examples.bankplus.transactions.services.client.Journal;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

@Stateless
public class PaymentService {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private Currencies currencies;

    @Inject
    private Accounts accounts;

    @Inject
    private Customers customers;

    @Inject
    private Journal journal;

    public Payment newOutgoingPayment(Customer from, Contact to, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new PaymentException("A positive amount should be specified for the payment.");
        }
        Currency USD = currencies.findByCode("USD");
        Money chargeAmount = new Money(USD, amount.multiply(new BigDecimal("0.005")));
        Money totalTransactionAmount = new Money(USD, amount).add(chargeAmount);
        if(from.getCustomerAccount().getFinancialAccount().getCurrentBalance().compareTo(totalTransactionAmount) == -1) {
            throw new PaymentException("Insufficient balance in the account");
        }

        Payment payment = new Payment();
        payment.setPayer(from);
        payment.setPayee(to);
        payment.setDateTime(new Date());
        payment.setPaymentAmount(new Money(USD, amount));
        Charge chargesForPayment = new Charge();
        chargesForPayment.setCollectedFrom(from.getCustomerAccount().getFinancialAccount());
        chargesForPayment.setDateTime(new Date());
        chargesForPayment.setChargeAmount(chargeAmount);

        payment.setDescription("Payment from " + payment.getPayer().getFullName() +
                " to " + payment.getPayee().getFullName());
        Set<JournalEntry> journalEntriesForPayment = prepareJournalEntries(payment);

        chargesForPayment.setDescription("Service charge for Payment from " + payment.getPayer().getFullName() +
                " to " + payment.getPayee().getFullName());
        Set<JournalEntry> journalEntriesForCharges = prepareJournalEntries(chargesForPayment, payment);
        em.persist(payment);
        em.persist(chargesForPayment);
        // Can be moved off into a batch
        journal.recordJournalEntries(journalEntriesForPayment);
        journal.recordJournalEntries(journalEntriesForCharges);
        return payment;
    }

    public IncomingPayment newIncomingPayment(IncomingPaymentMessage incomingPaymentMessage) {
        Currency USD = currencies.findByCode("USD");
        Customer customer = customers.findByIBAN(incomingPaymentMessage.getBeneficiary());
        if(customer == null) {
            // TODO Post the payment message to a failed message queue.
            // TODO How do institutions handle this?
            Logger.getLogger(PaymentService.class.getName()).severe("Failed to find customer account");
            return null;
        }
        Customer to = customer;

        IncomingPayment payment = new IncomingPayment();
        payment.setPayer(incomingPaymentMessage.getOrderingCustomer());
        payment.setPayee(to);
        payment.setDateTime(new Date());
        Money paymentAmount = incomingPaymentMessage.getAmount();
        Money chargeAmount = new Money(USD, paymentAmount.getAmount().multiply(new BigDecimal("0.005")));
        payment.setPaymentAmount(paymentAmount);
        Charge chargesForPayment = new Charge();
        chargesForPayment.setCollectedFrom(customer.getCustomerAccount().getFinancialAccount());
        chargesForPayment.setDateTime(new Date());
        chargesForPayment.setChargeAmount(chargeAmount);
        payment.setDescription("Incoming payment from " + payment.getPayer() +
                " to " + payment.getPayee().getFullName());
        Set<JournalEntry> journalEntriesForPayment = prepareJournalEntries(payment);
        chargesForPayment.setDescription("Service charge for Incoming payment from " + payment.getPayer() +
                " to " + payment.getPayee().getFullName());
        Set<JournalEntry> journalEntriesForCharges = prepareJournalEntries(chargesForPayment, payment);

        payment.setIncomingPaymentMessage(incomingPaymentMessage);

        em.persist(payment);
        em.persist(chargesForPayment);
        em.persist(incomingPaymentMessage);
        // Can be moved off into a batch
        journal.recordJournalEntries(journalEntriesForPayment);
        journal.recordJournalEntries(journalEntriesForCharges);
        return payment;
    }

    private Set<JournalEntry> prepareJournalEntries(Payment payment) {
        Money amount = payment.getPaymentAmount();
        Customer internalCustomer = customers.findByIBAN(payment.getPayee().getIban());
        Account payeeAccount = null;
        if(internalCustomer == null) {
            Account clearingAccount = accounts.getClearingAccount();
            if(clearingAccount == null) {
                throw new PaymentException("Failed to find a clearing house for the contact.");
            } else {
                payeeAccount = clearingAccount;
                generatePaymentMessage(payment);
            }
        } else {
            payeeAccount = internalCustomer.getCustomerAccount().getFinancialAccount();
        }
        Currency USD = currencies.findByCode("USD");

        final JournalEntry creditEntry = new JournalEntry();
        creditEntry.setAccount(payeeAccount);
        creditEntry.setType(EntryType.CREDIT);
        creditEntry.setAmount(amount);
        creditEntry.setDateTime(payment.getDateTime());
        creditEntry.setEventReference(payment.getId());
        creditEntry.setDescription(payment.getDescription());
        final JournalEntry debitEntry = new JournalEntry();
        debitEntry.setType(EntryType.DEBIT);
        debitEntry.setAccount(payment.getPayer().getCustomerAccount().getFinancialAccount());
        debitEntry.setAmount(amount);
        debitEntry.setDateTime(payment.getDateTime());
        debitEntry.setEventReference(payment.getId());
        debitEntry.setDescription(payment.getDescription());
        final Set<JournalEntry> journalEntries = new HashSet<>();
        journalEntries.add(debitEntry);
        journalEntries.add(creditEntry);
        return journalEntries;
    }

    private Set<JournalEntry> prepareJournalEntries(Charge chargesForPayment, Payment payment) {
        Currency USD = currencies.findByCode("USD");
        Money charges = chargesForPayment.getChargeAmount();
        final JournalEntry chargesEntry = new JournalEntry();
        chargesEntry.setType(EntryType.CREDIT);
        chargesEntry.setAccount(accounts.getChargesAccount());
        chargesEntry.setAmount(charges);
        chargesEntry.setDateTime(chargesForPayment.getDateTime());
        chargesEntry.setEventReference(chargesForPayment.getId());
        chargesEntry.setDescription(chargesForPayment.getDescription());
        final JournalEntry deductChargesEntry = new JournalEntry();
        deductChargesEntry.setType(EntryType.DEBIT);
        deductChargesEntry.setAccount(chargesForPayment.getCollectedFrom());
        deductChargesEntry.setAmount(charges);
        deductChargesEntry.setDateTime(chargesForPayment.getDateTime());
        deductChargesEntry.setEventReference(chargesForPayment.getId());
        deductChargesEntry.setDescription(chargesForPayment.getDescription());
        final Set<JournalEntry> journalEntries = new HashSet<>();
        journalEntries.add(chargesEntry);
        journalEntries.add(deductChargesEntry);
        return journalEntries;
    }

    private Set<JournalEntry> prepareJournalEntries(IncomingPayment payment) {
        Money amount = payment.getPaymentAmount();
        Account payeeAccount = accounts.getClearingAccount();
        if(payeeAccount == null) {
            throw new PaymentException("Failed to find a clearing house for the contact.");
        }
        Currency USD = currencies.findByCode("USD");

        final JournalEntry creditEntry = new JournalEntry();
        creditEntry.setAccount(payment.getPayee().getCustomerAccount().getFinancialAccount());
        creditEntry.setType(EntryType.CREDIT);
        creditEntry.setAmount(amount);
        creditEntry.setDateTime(payment.getDateTime());
        creditEntry.setEventReference(payment.getId());
        creditEntry.setDescription(payment.getDescription());
        final JournalEntry debitEntry = new JournalEntry();
        debitEntry.setType(EntryType.DEBIT);
        debitEntry.setAccount(payeeAccount);
        debitEntry.setAmount(amount);
        debitEntry.setDateTime(payment.getDateTime());
        debitEntry.setEventReference(payment.getId());
        debitEntry.setDescription(payment.getDescription());
        final Set<JournalEntry> journalEntries = new HashSet<>();
        journalEntries.add(debitEntry);
        journalEntries.add(creditEntry);
        return journalEntries;
    }

    private Set<JournalEntry> prepareJournalEntries(Charge chargesForPayment, IncomingPayment payment) {
        Currency USD = currencies.findByCode("USD");
        Money charges = chargesForPayment.getChargeAmount();
        final JournalEntry chargesEntry = new JournalEntry();
        chargesEntry.setType(EntryType.CREDIT);
        chargesEntry.setAccount(accounts.getChargesAccount());
        chargesEntry.setAmount(charges);
        chargesEntry.setDateTime(chargesForPayment.getDateTime());
        chargesEntry.setEventReference(chargesForPayment.getId());
        chargesEntry.setDescription(payment.getDescription());
        final JournalEntry deductChargesEntry = new JournalEntry();
        deductChargesEntry.setType(EntryType.DEBIT);
        deductChargesEntry.setAccount(chargesForPayment.getCollectedFrom());
        deductChargesEntry.setAmount(charges);
        deductChargesEntry.setDateTime(chargesForPayment.getDateTime());
        deductChargesEntry.setEventReference(chargesForPayment.getId());
        deductChargesEntry.setDescription(payment.getDescription());
        final Set<JournalEntry> journalEntries = new HashSet<>();
        journalEntries.add(chargesEntry);
        journalEntries.add(deductChargesEntry);
        return journalEntries;
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
