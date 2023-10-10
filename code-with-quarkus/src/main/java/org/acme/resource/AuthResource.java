package org.acme.resource;


import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.acme.Constant.StatusCode;
import org.acme.Dto.LoginDto;
import org.acme.responses.CustomResponse;
import org.acme.service.AuthService;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Metered;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.jboss.resteasy.reactive.RestForm;


@Path("/auth")
@Slf4j
public class AuthResource {

    @Inject
    private AuthService authService;


    @Path("/login")
    @POST
    @Fallback(fallbackMethod = "loginFallback")
    @Retry(maxRetries = 2)
    //@Timeout(value = 2000)
    //@Counted
   // @Timed
    public Response login(LoginDto loginDto) {
        //log.info("inside login method");
        return authService.login(loginDto).responseBuilder();
    }

    @Path("/logout")
    @POST
    @Fallback(fallbackMethod = "logoutFallback")
    public Response logout(@QueryParam("refresh_token") String refresh_token){
        //log.error("inside logout "+refresh_token);
        return authService.logout("reactApp",refresh_token).responseBuilder();
    }

    @Path("/refreshtoken")
    @POST
    public Response refreshToken(@QueryParam("refresh_token") String refresh_token){
        log.error("refresh_token "+refresh_token);
        return authService.refreshToken("reactApp",refresh_token,"refresh_token").responseBuilder();
    }


    public Response loginFallback(LoginDto loginDto){
        //log.info("");
        return CustomResponse
                .getBuilder()
                .statusCode(StatusCode.INTERNAL_SERVER_ERROR)
                .message("An error occured! Please try again!")
                .responseBuilder();
    }

    public Response logoutFallback(String refresh_token){
        return CustomResponse
                .getBuilder()
                .statusCode(StatusCode.INTERNAL_SERVER_ERROR)
                .message("An error occured! Please try again!")
                .responseBuilder();
    }
}
