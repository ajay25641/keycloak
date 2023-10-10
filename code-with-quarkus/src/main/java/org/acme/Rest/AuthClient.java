package org.acme.Rest;


import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import org.acme.Dto.TokenResponse;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.reactive.RestForm;


@Path(value="/realms/testDev/protocol/openid-connect")
@RegisterRestClient(configKey = "keycloakUrl")
public interface AuthClient {

   @Path("/token")
   @POST
   public TokenResponse login(
           @FormParam("grant_type") String grantType,
           @FormParam("client_id") String clientId,
           @FormParam("username")  String username,
           @FormParam("password") String password,
           @FormParam("scope") String scope
   );

   @Path("/logout")
   @POST
   @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
   public void logout(@RestForm("client_id") String client_id,@RestForm("refresh_token") String refresh_token);


   @Path("/token")
   @POST
   @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
   public TokenResponse refreshToken(@RestForm("client_id") String client_id, @RestForm("refresh_token") String refresh_token,@RestForm String grant_type);

}
