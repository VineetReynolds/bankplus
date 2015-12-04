package org.jboss.examples.bankplus.reporting.services.adapters;

import org.jboss.examples.bankplus.reporting.services.client.Customer;
import org.jboss.examples.bankplus.reporting.services.translators.CustomerTranslator;

import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

public class CustomerAdapter {

    @Inject
    private CustomerTranslator translator;

    private static final String host;

    private static final int port;

    static {
        String envHost = System.getenv("CUSTOMERS_PORT_8080_TCP_ADDR");
        host = envHost == null ? "bankplus_customers.dev.docker" : envHost;
        String envPort = System.getenv("CUSTOMERS_PORT_8080_TCP_PORT");
        port = envPort == null ? 8080 : Integer.parseInt(envPort);
    }

    public org.jboss.examples.bankplus.reporting.model.Customer findById(Long customerId) {
        Client client = ClientBuilder.newClient();
        UriBuilder builder = UriBuilder.fromUri("http://{host}:{port}/bankplus-customers/rest/")
                .path("customers/{customerId}")
                .resolveTemplate("host", host)
                .resolveTemplate("port", port)
                .resolveTemplate("customerId", customerId);
        WebTarget target = client.target(builder);
        Customer customer = target.request(MediaType.APPLICATION_JSON_TYPE).get(Customer.class);
        return translator.translate(customer);
    }

    public org.jboss.examples.bankplus.reporting.model.Customer findByIBAN(String iban) {
        Client client = ClientBuilder.newClient();
        UriBuilder builder = UriBuilder.fromUri("http://{host}:{port}/bankplus-customers/rest/")
                .path("customers/")
                .queryParam("iban", iban)
                .resolveTemplate("host", host)
                .resolveTemplate("port", port);
        WebTarget target = client.target(builder);
        Customer customer = target.request(MediaType.APPLICATION_JSON_TYPE).get(Customer.class);
        return translator.translate(customer);
    }
}
