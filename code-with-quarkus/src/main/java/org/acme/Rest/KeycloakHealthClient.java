package org.acme.Rest;


import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "keycloakUrl")
public interface KeycloakHealthClient {

    @GET
    @Path("/health")
    Response checkHealth();
}
