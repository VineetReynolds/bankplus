package org.jboss.examples.bankplus.keycloak.providers;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.keycloak.connections.httpclient.HttpClientBuilder;
import org.keycloak.events.Event;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.EventType;
import org.keycloak.events.admin.AdminEvent;
import org.keycloak.models.*;
import org.keycloak.util.KeycloakUriBuilder;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.IOException;

public class BankplusEventListenerProvider implements EventListenerProvider {

    private final KeycloakSession session;
    private RealmProvider model;

    public BankplusEventListenerProvider(KeycloakSession session) {
        this.session = session;
        this.model = session.realms();
    }

    @Override
    public void onEvent(Event event) {

        // Listen only for registration and login events.
        // Both KeyCloak and Social logins are supported.
        if (event.getType() == EventType.REGISTER || event.getType() == EventType.LOGIN || event.getType() == EventType.IDENTITY_PROVIDER_LOGIN) {

            if (event.getRealmId() != null && event.getUserId() != null) {
                RealmModel realm = model.getRealm(event.getRealmId());
                UserModel user = session.users().getUserById(event.getUserId(), realm);
                if (user != null && user.getEmail() != null) {
                    String fullName = user.getFirstName() + " " + user.getLastName();
                    String email = user.getEmail();

                    HttpClient client = new HttpClientBuilder()
                            .disableTrustManager().build();

                    try {
                        // Query for the user in the BankPlus REST API
                        HttpGet get = new HttpGet(
                                KeycloakUriBuilder.fromUri("http://localhost:9080/bankplus")
                                        .path("/rest/customers/").queryParam("email", email).build());
                        HttpResponse isUserRegisteredResponse = client.execute(get);
                        if (isUserRegisteredResponse.getStatusLine().getStatusCode() != 200) {
                            // Possible failure? Close the KeyCloak session
                            // This prevents the user from accessing the application if there is an error during on-boarding.
                            UserSessionModel userSession = session.sessions().getUserSession(realm, event.getSessionId());
                            session.sessions().removeUserSession(realm, userSession);
                            throw new RuntimeException("Failed : HTTP error code : "
                                    + isUserRegisteredResponse.getStatusLine().getStatusCode());
                        }

                        // If the user does not exist, then create it through the API
                        JsonReader reader = Json.createReader(isUserRegisteredResponse.getEntity().getContent());
                        JsonArray customers = reader.readArray();
                        if (customers.isEmpty()) {

                            HttpPost post = new HttpPost(
                                    KeycloakUriBuilder.fromUri("http://localhost:9080/bankplus")
                                            .path("/rest/customers/").build());

                            JsonObject customer = Json.createObjectBuilder()
                                    .add("fullName", fullName)
                                    .add("emailAddress", email)
                                    .build();

                            StringEntity input = new StringEntity(customer.toString());
                            input.setContentType("application/json");
                            post.setEntity(input);

                            HttpResponse userCreatedResponse = client.execute(post);

                            if (userCreatedResponse.getStatusLine().getStatusCode() != 201) {
                                // Possible failure? Close the KeyCloak session
                                // This prevents the user from accessing the application if there is an error during on-boarding.
                                UserSessionModel userSession = session.sessions().getUserSession(realm, event.getSessionId());
                                session.sessions().removeUserSession(realm, userSession);
                                throw new RuntimeException("Failed : HTTP error code : "
                                        + userCreatedResponse.getStatusLine().getStatusCode());
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        client.getConnectionManager().shutdown();
                    }
                }
            }
        }
    }

    @Override
    public void onEvent(AdminEvent adminEvent, boolean b) {
        // Do nothing
    }

    @Override
    public void close() {
        // Do nothing
    }
}
