package org.jboss.examples.bankplus.messages.services;

import org.jboss.examples.bankplus.messages.model.IncomingPaymentMessage;
import org.jboss.examples.bankplus.messages.services.client.IncomingPayment;
import org.jboss.examples.bankplus.messages.services.client.IncomingPayments;
import org.jboss.examples.bankplus.money.model.CurrencyAdapter;
import org.jboss.examples.bankplus.money.services.Currencies;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.*;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.util.logging.Logger;

@JMSDestinationDefinition(name = "java:jboss/jms/queue/IncomingPaymentsQueue",
        interfaceName = "javax.jms.Queue",
        destinationName = "IncomingPaymentsQueue")
@MessageDriven(name = "HelloWorldQueueMDB", activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "queue/IncomingPaymentsQueue"),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
        @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge") })
public class IncomingPaymentProcessor implements MessageListener {

    private final static Logger LOGGER = Logger.getLogger(IncomingPaymentProcessor.class.toString());

    @Inject
    private IncomingPayments incomingPayments;

    @Inject
    private Currencies currencies;

    @Override
    public void onMessage(Message message) {
        TextMessage textMessage = null;
        try {
            if (message instanceof TextMessage) {
                textMessage = (TextMessage) message;
                String paymentMessageText = textMessage.getText();
                IncomingPaymentMessage paymentMessage = null;
                try {
                    JAXBContext jaxbContext = JAXBContext.newInstance(IncomingPaymentMessage.class);
                    Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                    CurrencyAdapter currencyAdapter = new CurrencyAdapter();
                    currencyAdapter.setCurrencies(currencies);
                    jaxbUnmarshaller.setAdapter(currencyAdapter);

                    StringReader reader = new StringReader(paymentMessageText);
                    paymentMessage = (IncomingPaymentMessage) jaxbUnmarshaller.unmarshal(reader);
                } catch (JAXBException jaxbEx) {
                    throw new RuntimeException(jaxbEx);
                }
                incomingPayments.newIncomingPayment(new IncomingPayment(paymentMessage));
            } else {
                LOGGER.warning("Message of wrong type: " + message.getClass().getName());
            }
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }
}
