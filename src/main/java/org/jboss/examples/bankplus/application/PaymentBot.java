package org.jboss.examples.bankplus.application;

import com.google.common.collect.ImmutableList;
import org.jboss.examples.bankplus.model.customer.CustomerAccount;
import org.jboss.examples.bankplus.model.messages.IncomingPaymentMessage;
import org.jboss.examples.bankplus.model.money.Currency;
import org.jboss.examples.bankplus.model.money.Money;
import org.jboss.examples.bankplus.services.Currencies;
import org.jboss.examples.bankplus.services.CustomerAccounts;

import javax.annotation.Resource;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.JMSContext;
import javax.jms.Queue;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Stateless
public class PaymentBot {

    private static final ImmutableList<String> PAYERS = ImmutableList.<String>builder()
            .add("Alice")
            .add("Bob")
            .add("Charlie")
            .add("Dave")
            .add("Eve")
            .add("Frank")
            .build();

    @PersistenceContext
    private EntityManager em;

    @Inject
    private CustomerAccounts customerAccounts;

    @Inject
    private Currencies currencies;

    @Inject
    private JMSContext jmsContext;

    @Resource(mappedName = "java:jboss/jms/queue/IncomingPaymentsQueue")
    private Queue incomingPaymentsQueue;

    @Schedule(minute = "*/1", hour = "*")
    public void schedulePayment(){
        Currency USD = currencies.findByCode("USD");

        List<CustomerAccount> allAccounts = customerAccounts.listAll();
        Random random = new Random();
        int numAccounts = allAccounts.size();
        if (numAccounts < 1) {
            return;
        }
        int randomPick = random.nextInt(numAccounts);
        CustomerAccount customerAccount = allAccounts.get(randomPick);

        String randomSender = PAYERS.get(random.nextInt(PAYERS.size()));

        BigDecimal amountToPay = new BigDecimal(random.nextInt(10) + 1);

        IncomingPaymentMessage incomingPaymentMessage = new IncomingPaymentMessage();
        incomingPaymentMessage.setOrderingCustomer(randomSender);
        incomingPaymentMessage.setBeneficiary(customerAccount.getIban());
        incomingPaymentMessage.setBookingDate(new Date());
        incomingPaymentMessage.setAmount(new Money(USD, amountToPay));

        incomingPaymentMessage.generate();

        String paymentMessageText = incomingPaymentMessage.getMessageText();
        jmsContext.createProducer().send(incomingPaymentsQueue, paymentMessageText);
    }
}
