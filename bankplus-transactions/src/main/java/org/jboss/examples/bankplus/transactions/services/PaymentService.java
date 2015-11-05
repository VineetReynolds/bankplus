package org.jboss.examples.bankplus.transactions.services;

import org.jboss.examples.bankplus.money.model.Currency;
import org.jboss.examples.bankplus.money.model.Money;
import org.jboss.examples.bankplus.money.services.Currencies;
import org.jboss.examples.bankplus.transactions.model.Account;
import org.jboss.examples.bankplus.transactions.model.*;
import org.jboss.examples.bankplus.transactions.model.Contact;
import org.jboss.examples.bankplus.transactions.model.Customer;
import org.jboss.examples.bankplus.transactions.services.client.*;

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
    private Customers customers;

    @Inject
    private Journal journal;

    @Inject
    private OutgoingPaymentMessages outgoingPaymentMessages;

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

        Customer internalCustomer = customers.findByIBAN(payment.getPayee().getIban());
        boolean externalTransfer = internalCustomer == null ? true: false;

        payment.setDescription("Payment from " + payment.getPayer().getFullName() +
                " to " + payment.getPayee().getFullName());
        Set<JournalEntry> journalEntriesForPayment = prepareJournalEntries(payment, internalCustomer);

        chargesForPayment.setDescription("Service charge for Payment from " + payment.getPayer().getFullName() +
                " to " + payment.getPayee().getFullName());
        Set<JournalEntry> journalEntriesForCharges = prepareJournalEntries(chargesForPayment, payment);
        em.persist(payment);
        em.persist(chargesForPayment);
        // Can be moved off into a batch
        if(externalTransfer) {
            generatePaymentMessage(payment);
        }
        journal.recordJournalEntries(journalEntriesForPayment);
        journal.recordJournalEntries(journalEntriesForCharges);
        return payment;
    }

    public IncomingPayment newIncomingPayment(Customer to, String payer, Money amount) {
        Currency USD = currencies.findByCode("USD");

        IncomingPayment payment = new IncomingPayment();
        //payment.setPayer(incomingPaymentMessage.getOrderingCustomer());
        payment.setPayer(payer);
        payment.setPayee(to);
        payment.setDateTime(new Date());
        Money paymentAmount = amount;
        Money chargeAmount = new Money(USD, paymentAmount.getAmount().multiply(new BigDecimal("0.005")));
        payment.setPaymentAmount(paymentAmount);
        Charge chargesForPayment = new Charge();
        chargesForPayment.setCollectedFrom(to.getCustomerAccount().getFinancialAccount());
        chargesForPayment.setDateTime(new Date());
        chargesForPayment.setChargeAmount(chargeAmount);
        payment.setDescription("Incoming payment from " + payment.getPayer() +
                " to " + payment.getPayee().getFullName());
        Set<JournalEntry> journalEntriesForPayment = prepareJournalEntries(payment);
        chargesForPayment.setDescription("Service charge for Incoming payment from " + payment.getPayer() +
                " to " + payment.getPayee().getFullName());
        Set<JournalEntry> journalEntriesForCharges = prepareJournalEntries(chargesForPayment, payment);

        em.persist(payment);
        em.persist(chargesForPayment);
        // Can be moved off into a batch
        journal.recordJournalEntries(journalEntriesForPayment);
        journal.recordJournalEntries(journalEntriesForCharges);
        return payment;
    }

    private void generatePaymentMessage(Payment payment) {
        OutgoingPaymentMessage paymentMessage = new OutgoingPaymentMessage();
        paymentMessage.setOrderingCustomer(payment.getPayer().getFullName());
        paymentMessage.setBeneficiary(payment.getPayee().getIban());
        paymentMessage.setAmount(payment.getPaymentAmount());
        paymentMessage.setBookingDate(payment.getDateTime());
        outgoingPaymentMessages.publishMessage(paymentMessage);
    }

    private Set<JournalEntry> prepareJournalEntries(Payment payment, Customer internalCustomer) {
        Money amount = payment.getPaymentAmount();
        Account payeeAccount = null;
        if(internalCustomer == null) {
            Account clearingAccount = accounts.getClearingAccount();
            if(clearingAccount == null) {
                throw new PaymentException("Failed to find a clearing house for the contact.");
            } else {
                payeeAccount = clearingAccount;
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

}
