package org.acme.service;


import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import org.acme.Constant.StatusCode;
import org.acme.Dto.LoginDto;
import org.acme.Dto.TokenResponse;
import org.acme.Rest.AuthClient;
import org.acme.responses.CustomResponse;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@ApplicationScoped
@Slf4j
public class AuthService {
    @RestClient
    private AuthClient authClient;

    public CustomResponse login(LoginDto loginDto){
        loginDto.setClient_id("reactApp");
        loginDto.setGrant_type("password");
        loginDto.setScope("openid");

        try{
            TokenResponse tokenResponse=authClient.login(
                    loginDto.getGrant_type(),
                    loginDto.getClient_id(),
                    loginDto.getUsername(),
                    loginDto.getPassword(),
                    loginDto.getScope());

            return CustomResponse
                    .getBuilder()
                    .statusCode(StatusCode.OK)
                    .message("User successfully logged In.")
                    .data(tokenResponse)
                    .build();
        }
        catch (Exception ex){
           return CustomResponse
                    .getBuilder()
                    .message("Unable to login!")
                    .statusCode(StatusCode.BAD_REQUEST)
                    .errorMessages(ex.getMessage())
                    .build();
        }

      //throw new RuntimeException("for login");
    }
    public CustomResponse logout(String client_id,String refresh_token){

        try{
            authClient.logout(client_id,refresh_token);
            return CustomResponse
                    .getBuilder()
                    .message("User successfully logged out.")
                    .statusCode(StatusCode.OK)
                    .build();
        }
        catch (Exception e){
            log.error(e.getMessage());
            return CustomResponse
                    .getBuilder()
                    .message("Unable to logout user!")
                    .errorMessages(e.getMessage())
                    .statusCode(StatusCode.BAD_REQUEST)
                    .build();
        }
    }
    public CustomResponse refreshToken(String client_id, String refresh_token, String grant_type){
        try{

           TokenResponse tokenResponse= authClient.refreshToken(client_id,refresh_token,grant_type);
           return CustomResponse
                   .getBuilder()
                   .message("Token refreshed successfully")
                   .statusCode(StatusCode.OK)
                   .data(tokenResponse)
                   .build();
        }
        catch (Exception ex){
            return CustomResponse
                    .getBuilder()
                    .message("Unable to refresh token!")
                    .statusCode(StatusCode.BAD_REQUEST)
                    .errorMessages(ex.getMessage())
                    .build();
        }
    }
}
