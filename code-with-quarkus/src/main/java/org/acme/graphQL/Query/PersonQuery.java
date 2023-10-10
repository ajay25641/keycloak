package org.acme.graphQL.Query;


import io.quarkus.security.Authenticated;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.ws.rs.*;
import org.acme.Model.Person;
import org.acme.Validations.ValidParam;
import org.acme.graphQL.service.PersonService;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Mutation;
import org.eclipse.microprofile.graphql.Query;
import org.eclipse.microprofile.graphql.Source;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.keycloak.representations.idm.UserRepresentation;
import java.util.List;

@GraphQLApi
public class PersonQuery {

    @Inject
    private PersonService personService;

    @Inject
    private JsonWebToken jwt;


    @Mutation("createuser")
    public UserRepresentation createUser(@Valid @Source Person person){
        return personService.createUser(person);
    }

    @RolesAllowed("admin")
    @Query("allusers")
    public List<UserRepresentation> getAllUser() throws InterruptedException {
        return personService.getAllUsers();
    }

    @Query("getuserbyid")
    @Authenticated
    public UserRepresentation getUserById(){
        String userId=jwt.getSubject();
        return personService.getUserById(userId);
    }

    @Query("getuserbyfieldname")
    @RolesAllowed("admin")
    public List<UserRepresentation> getUserByFieldName(
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
            throw new RuntimeException("field required!");
        }
        return personService.getUserByFieldName(fieldVal,fieldName);
    }
    @Authenticated
    @Mutation("updateuser")
    public UserRepresentation updateUser(Person person) {
        String userId=jwt.getSubject();
        return personService.updateUser(userId,person);
    }

    @RolesAllowed("admin")
    @Mutation("delete")
    public void deleteUser(@ValidParam @QueryParam("id") String id){
        personService.deleteUser(id);
    }
}

