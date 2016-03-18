FROM jboss/keycloak:1.7.0.Final

USER jboss

ADD . /tmp/bankplus-provider

# Set the JBOSS_HOME env variable
ENV JBOSS_HOME /opt/jboss/keycloak

# Add the BankPlus Keycloak provider module
RUN /opt/jboss/keycloak/bin/jboss-cli.sh --command="module add --name=org.jboss.examples.demo --resources=/tmp/bankplus-provider/target/keycloak-provider.jar --module-xml=/tmp/bankplus-provider/src/main/etc/module.xml"
# Modify keycloak-server.json - adds the module to the providers array using the jq cli tool. Write the output to a file and overwrite the existing keycloak-server.json
RUN jq '.providers |= (. + ["module:org.jboss.examples.demo"] | unique)' /opt/jboss/keycloak/standalone/configuration/keycloak-server.json > tmp.json && mv -f tmp.json /opt/jboss/keycloak/standalone/configuration/keycloak-server.json

# Copy the Keycloak realm file to Keycloak home dir
RUN cp /tmp/bankplus-provider/BankPlus-realm.json /opt/jboss/keycloak

# Start Keycloak
CMD ["/opt/jboss/keycloak/bin/standalone.sh", "-b", "0.0.0.0" , "-Dkeycloak.import=/opt/jboss/keycloak/BankPlus-realm.json"]
