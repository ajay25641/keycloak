package org.acme.config;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.Getter;
import org.eclipse.microprofile.config.inject.ConfigProperty;


@Getter
@ApplicationScoped
public class PropertyConfig {

    @Inject
    @ConfigProperty(name = "quarkus.keycloak.auth-server-url")
    private String serverUrl="quarkus.keycloak.auth-server-url";

    @Inject
    @ConfigProperty(name = "quarkus.keycloak.realm")
    private String realm;

    @Inject
    @ConfigProperty(name="quarkus.keycloak.resource")
    private String clientId;

    @Inject
    @ConfigProperty(name = "quarkus.keycloak.credentials.secret")
    private String clientSecret;



}
