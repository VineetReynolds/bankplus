package org.jboss.examples.bankplus.transactions.services;

import org.jboss.examples.bankplus.accounting.model.Account;
import org.jboss.examples.bankplus.accounting.model.EntryType;
import org.jboss.examples.bankplus.accounting.model.JournalEntry;
import org.jboss.examples.bankplus.accounting.model.PostingStatus;
import org.jboss.examples.bankplus.accounting.services.Accounts;
import org.jboss.examples.bankplus.accounting.services.Journal;
import org.jboss.examples.bankplus.customer.model.Contact;
import org.jboss.examples.bankplus.customer.model.Customer;
import org.jboss.examples.bankplus.customer.model.CustomerAccount;
import org.jboss.examples.bankplus.messages.model.IncomingPaymentMessage;
import org.jboss.examples.bankplus.messages.model.OutgoingPaymentMessage;
import org.jboss.examples.bankplus.money.model.Currency;
import org.jboss.examples.bankplus.money.model.Money;
import org.jboss.examples.bankplus.money.services.Currencies;
import org.jboss.examples.bankplus.customer.services.CustomerAccounts;
import org.jboss.examples.bankplus.transactions.model.Charge;
import org.jboss.examples.bankplus.transactions.model.IncomingPayment;
import org.jboss.examples.bankplus.transactions.model.Payment;

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
    private CustomerAccounts customerAccounts;

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
        postJournalEntries(payment);
        postJournalEntries(chargesForPayment, payment);
        em.persist(payment);
        em.persist(chargesForPayment);
        // Can be moved off into a batch
        journal.postToLedger(payment.getJournalEntries());
        journal.postToLedger(chargesForPayment.getJournalEntries());
        return payment;
    }

    public IncomingPayment newIncomingPayment(IncomingPaymentMessage incomingPaymentMessage) {
        Currency USD = currencies.findByCode("USD");
        CustomerAccount customerAccount = customerAccounts.findByIBAN(incomingPaymentMessage.getBeneficiary());
        if(customerAccount == null) {
            // TODO Post the payment message to a failed message queue.
            // TODO How do institutions handle this?
            Logger.getLogger(PaymentService.class.getName()).severe("Failed to find customer account");
            return null;
        }
        Customer to = customerAccount.getCustomer();

        IncomingPayment payment = new IncomingPayment();
        payment.setPayer(incomingPaymentMessage.getOrderingCustomer());
        payment.setPayee(to);
        payment.setDateTime(new Date());
        Money paymentAmount = incomingPaymentMessage.getAmount();
        Money chargeAmount = new Money(USD, paymentAmount.getAmount().multiply(new BigDecimal("0.005")));
        payment.setPaymentAmount(paymentAmount);
        Charge chargesForPayment = new Charge();
        chargesForPayment.setCollectedFrom(customerAccount.getFinancialAccount());
        chargesForPayment.setDateTime(new Date());
        chargesForPayment.setChargeAmount(chargeAmount);
        postJournalEntries(payment);
        postJournalEntries(chargesForPayment, payment);

        payment.setIncomingPaymentMessage(incomingPaymentMessage);

        em.persist(payment);
        em.persist(chargesForPayment);
        em.persist(incomingPaymentMessage);
        // Can be moved off into a batch
        journal.postToLedger(payment.getJournalEntries());
        journal.postToLedger(chargesForPayment.getJournalEntries());
        return payment;
    }

    private void postJournalEntries(Payment payment) {
        Money amount = payment.getPaymentAmount();
        CustomerAccount internalCustomerAccount = customerAccounts.findByIBAN(payment.getPayee().getIban());
        Account payeeAccount = null;
        if(internalCustomerAccount == null) {
            Account clearingAccount = accounts.getClearingAccount();
            if(clearingAccount == null) {
                throw new PaymentException("Failed to find a clearing house for the contact.");
            } else {
                payeeAccount = clearingAccount;
                generatePaymentMessage(payment);
            }
        } else {
            payeeAccount = internalCustomerAccount.getFinancialAccount();
        }
        Currency USD = currencies.findByCode("USD");

        final JournalEntry creditEntry = new JournalEntry();
        creditEntry.setAccount(payeeAccount);
        creditEntry.setType(EntryType.CREDIT);
        creditEntry.setAmount(amount);
        creditEntry.setDateTime(payment.getDateTime());
        creditEntry.setFinancialEvent(payment);
        creditEntry.setPostingStatus(PostingStatus.UNPOSTED);
        final JournalEntry debitEntry = new JournalEntry();
        debitEntry.setType(EntryType.DEBIT);
        debitEntry.setAccount(payment.getPayer().getCustomerAccount().getFinancialAccount());
        debitEntry.setAmount(amount);
        debitEntry.setDateTime(payment.getDateTime());
        debitEntry.setFinancialEvent(payment);
        debitEntry.setPostingStatus(PostingStatus.UNPOSTED);
        final Set<JournalEntry> journalEntries = new HashSet<>();
        journalEntries.add(debitEntry);
        journalEntries.add(creditEntry);

        String description = "Payment from " + payment.getPayer().getFullName() +
                " to " + payment.getPayee().getFullName();

        payment.setDescription(description);
        payment.setJournalEntries(journalEntries);
    }

    private void postJournalEntries(Charge chargesForPayment, Payment payment) {
        Currency USD = currencies.findByCode("USD");
        Money charges = chargesForPayment.getChargeAmount();
        final JournalEntry chargesEntry = new JournalEntry();
        chargesEntry.setType(EntryType.CREDIT);
        chargesEntry.setAccount(accounts.getChargesAccount());
        chargesEntry.setAmount(charges);
        chargesEntry.setDateTime(chargesForPayment.getDateTime());
        chargesEntry.setFinancialEvent(chargesForPayment);
        chargesEntry.setPostingStatus(PostingStatus.UNPOSTED);
        final JournalEntry deductChargesEntry = new JournalEntry();
        deductChargesEntry.setType(EntryType.DEBIT);
        deductChargesEntry.setAccount(chargesForPayment.getCollectedFrom());
        deductChargesEntry.setAmount(charges);
        deductChargesEntry.setDateTime(chargesForPayment.getDateTime());
        deductChargesEntry.setFinancialEvent(chargesForPayment);
        deductChargesEntry.setPostingStatus(PostingStatus.UNPOSTED);
        final Set<JournalEntry> journalEntries = new HashSet<>();
        journalEntries.add(chargesEntry);
        journalEntries.add(deductChargesEntry);

        String description = "Service charge for Payment from " + payment.getPayer().getFullName() +
                " to " + payment.getPayee().getFullName();

        chargesForPayment.setDescription(description);
        chargesForPayment.setJournalEntries(journalEntries);
    }

    private void postJournalEntries(IncomingPayment payment) {
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
        creditEntry.setFinancialEvent(payment);
        creditEntry.setPostingStatus(PostingStatus.UNPOSTED);
        final JournalEntry debitEntry = new JournalEntry();
        debitEntry.setType(EntryType.DEBIT);
        debitEntry.setAccount(payeeAccount);
        debitEntry.setAmount(amount);
        debitEntry.setDateTime(payment.getDateTime());
        debitEntry.setFinancialEvent(payment);
        debitEntry.setPostingStatus(PostingStatus.UNPOSTED);
        final Set<JournalEntry> journalEntries = new HashSet<>();
        journalEntries.add(debitEntry);
        journalEntries.add(creditEntry);

        String description = "Incoming payment from " + payment.getPayer() +
                " to " + payment.getPayee().getFullName();

        payment.setDescription(description);
        payment.setJournalEntries(journalEntries);
    }

    private void postJournalEntries(Charge chargesForPayment, IncomingPayment payment) {
        Currency USD = currencies.findByCode("USD");
        Money charges = chargesForPayment.getChargeAmount();
        final JournalEntry chargesEntry = new JournalEntry();
        chargesEntry.setType(EntryType.CREDIT);
        chargesEntry.setAccount(accounts.getChargesAccount());
        chargesEntry.setAmount(charges);
        chargesEntry.setDateTime(chargesForPayment.getDateTime());
        chargesEntry.setFinancialEvent(chargesForPayment);
        chargesEntry.setPostingStatus(PostingStatus.UNPOSTED);
        final JournalEntry deductChargesEntry = new JournalEntry();
        deductChargesEntry.setType(EntryType.DEBIT);
        deductChargesEntry.setAccount(chargesForPayment.getCollectedFrom());
        deductChargesEntry.setAmount(charges);
        deductChargesEntry.setDateTime(chargesForPayment.getDateTime());
        deductChargesEntry.setFinancialEvent(chargesForPayment);
        deductChargesEntry.setPostingStatus(PostingStatus.UNPOSTED);
        final Set<JournalEntry> journalEntries = new HashSet<>();
        journalEntries.add(chargesEntry);
        journalEntries.add(deductChargesEntry);

        String description = "Service charge for Incoming payment from " + payment.getPayer() +
                " to " + payment.getPayee().getFullName();

        chargesForPayment.setDescription(description);
        chargesForPayment.setJournalEntries(journalEntries);
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
