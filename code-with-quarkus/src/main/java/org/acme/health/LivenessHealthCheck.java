package org.acme.health;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Response;
import org.acme.Rest.KeycloakHealthClient;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;
import org.eclipse.microprofile.rest.client.inject.RestClient;


@ApplicationScoped
@Liveness

public class LivenessHealthCheck implements HealthCheck {

    @RestClient
    KeycloakHealthClient healthClient;
    @Override
    public HealthCheckResponse call() {


        healthClient.checkHealth();
        return HealthCheckResponse.up("Keycloak is running");



    }
}
