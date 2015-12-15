package org.jboss.examples.bankplus.application;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.wildfly.swarm.container.Container;
import org.wildfly.swarm.jaxrs.JAXRSArchive;
import org.wildfly.swarm.messaging.MessagingFraction;

public class Main {

    public static void main(String[] args) throws Exception {
        Container container = new Container();

        container.fraction(MessagingFraction.createDefaultFraction()
                .defaultServer((s) -> {
                    s.jmsQueue("IncomingPaymentsQueue");
                }));

        // Start the container
        container.start();

        JAXRSArchive appDeployment = ShrinkWrap.create(JAXRSArchive.class);
        appDeployment.addPackage("org.jboss.examples.bankplus");

        // Deploy your app
        container.deploy(appDeployment);
    }
}
