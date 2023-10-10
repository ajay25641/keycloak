package org.acme.resource;


import io.quarkus.security.Authenticated;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.acme.Constant.StatusCode;
import org.acme.Model.Person;
import org.acme.Validations.ValidParam;
import org.acme.responses.CustomResponse;
import org.acme.service.PersonService;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.metrics.annotation.Counted;


@Path("/user")
public class PersonResource {

    @Inject
    private PersonService personService;

    @Inject
    private JsonWebToken jwt;

    @POST
    @Path("/createuser")
    @Counted
    public Response createUser(@Valid Person person){

        return personService
                .createUser(person)
                .responseBuilder();
    }

    @GET
    @Path("/admin/getallusers")
    @RolesAllowed("admin")
    public CustomResponse getAllUser() throws InterruptedException {
        Thread.sleep(2000);
        return personService
                .getAllUsers();

    }
    @GET
    @Path("/getuserbyid")
    @Authenticated
    //@Des
    public Response getUserById(){

        String userId=jwt.getSubject();

        return personService
                .getUserById(userId)
                .responseBuilder();
    }
    @GET
    @Path("/admin/getuserbyfieldname")
    @RolesAllowed("admin")
    @Fallback(fallbackMethod = "getUserByFieldNameFallback")
    public Response getUserByFieldName(
            @Email @QueryParam("email") String email,
            @ValidParam @QueryParam("firstName") String firstName,
            @ValidParam @QueryParam("lastName") String lastName,
            @ValidParam @QueryParam("username") String username){

        String fieldName;
        String fieldVal;

        if(email!=null){
            fieldVal=email;
            fieldName="email";
        }
        else if(firstName!=null){
            fieldVal=firstName;
            fieldName="firstName";
        }
        else if(lastName!=null){
            fieldVal=lastName;
            fieldName="lastName";
        }
        else if(username!=null){
            fieldVal=username;
            fieldName="username";
        }
        else{
            return CustomResponse.getBuilder()
                    .statusCode(StatusCode.BAD_REQUEST)
                    .message("Please provide the correct field Name!")
                    .responseBuilder();
        }
        return personService.getUserByFieldName(fieldVal,fieldName).responseBuilder();
    }
    @PUT
    @Path("/updateuser")
    @Authenticated
    public Response updateUser(Person person) {

//        if(person.getId()==null || person.getId().length()<=3){
//
//            return CustomResponse.getBuilder()
//                    .message("userId must not be blank!")
//                    .statusCode(StatusCode.BAD_REQUEST)
//                    .responseBuilder();
//        }
        String userId=jwt.getSubject();

        return personService
                .updateUser(userId,person)
                .responseBuilder();
    }


    @DELETE
    @Path("/admin/deleteuser")
    @RolesAllowed("admin")
    public Response deleteUser(@ValidParam @QueryParam("id") String id){

        return personService
                .deleteUser(id)
                .responseBuilder();
    }


    public Response getUserByFieldNameFallback(String email, String firstName, String lastName, String username){
        return CustomResponse
                .getBuilder()
                .statusCode(StatusCode.INTERNAL_SERVER_ERROR)
                .message("Unable to fetch user! Please try again later!")
                .responseBuilder();
    }


}
