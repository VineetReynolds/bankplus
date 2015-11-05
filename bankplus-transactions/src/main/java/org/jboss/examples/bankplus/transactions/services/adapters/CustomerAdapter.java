package org.jboss.examples.bankplus.transactions.services.adapters;

import org.jboss.examples.bankplus.transactions.services.client.Customer;
import org.jboss.examples.bankplus.transactions.services.translators.CustomerTranslator;

import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

public class CustomerAdapter {

    @Inject
    private CustomerTranslator translator;

    public org.jboss.examples.bankplus.transactions.model.Customer findById(Long customerId) {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target("http://localhost:9080/bankplus-customers/rest/").path("customers/{customerId}").resolveTemplate("customerId", customerId);
        Customer customer = target.request(MediaType.APPLICATION_JSON_TYPE).get(Customer.class);
        return translator.translate(customer);
    }

    public org.jboss.examples.bankplus.transactions.model.Customer findByIBAN(String iban) {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target("http://localhost:9080/bankplus-customers/rest/").path("customers/").queryParam("iban", iban);
        Customer customer = target.request(MediaType.APPLICATION_JSON_TYPE).get(Customer.class);
        return translator.translate(customer);
    }
}
