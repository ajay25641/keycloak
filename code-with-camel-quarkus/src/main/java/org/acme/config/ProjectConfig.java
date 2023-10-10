package org.acme.config;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Default;
import jakarta.inject.Inject;
import jakarta.ws.rs.Produces;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;


@ApplicationScoped
public class ProjectConfig {

    @Inject
    private PropertyConfig propertyConfig;

    @Produces
    @Default
    public Keycloak getKeycloak(){
       return KeycloakBuilder.builder()
                .serverUrl(propertyConfig.getServerUrl())
                .realm(propertyConfig.getRealm())
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .clientId(propertyConfig.getClientId())
                .clientSecret(propertyConfig.getClientSecret())
                .build();
    }
}
