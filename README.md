
Build
=====

Run `mvn clean package` to create the JAR file.


Deployment
==========

Install as a JBoss module in the KeyCloak server:

    KEYCLOAK_HOME/bin/jboss-cli.sh --command="module add --name=org.jboss.examples.bankplus --resources=target/event-listener-bankplus.jar --module-xml=src/main/etc/module.xml"

Then register the provider by editing keycloak-server.json and adding the module to the providers field:

    "providers": [
        "classpath:${jboss.server.config.dir}/providers/*",
        ...
        "module:org.jboss.examples.bankplus"
    ],

Then start (or restart) the server. Once started open the admin console, select your realm, then click on Events,
followed by config. Click on Listeners select box, then pick BankPlus-Broker from the dropdown. After this try to login to BankPlus
and verify that you were able to login successfully.

To uninstall:

    KEYCLOAK_HOME/bin/jboss-cli.sh --command="module remove --name=org.jboss.examples.bankplus"

and un-register the provider by editing keycloak-server.json, removing the previously added provider:

    "providers": [
        "classpath:${jboss.server.config.dir}/providers/*",
        ...
    ]