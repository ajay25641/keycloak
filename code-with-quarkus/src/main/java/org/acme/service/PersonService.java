package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.acme.Constant.StatusCode;
import org.acme.CustomException.DataNotFoundException;
import org.acme.Model.Person;
import org.acme.responses.CustomResponse;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@ApplicationScoped
public class PersonService {

    private String realm="testDev";
    @Inject
    private Keycloak keycloak;

    public CustomResponse createUser(Person person){

        UserRepresentation keyclockUser=new UserRepresentation();

        keyclockUser.setEmail(person.getEmail());
        keyclockUser.setFirstName(person.getFirstName());
        keyclockUser.setLastName(person.getLastName());
        keyclockUser.setUsername(person.getUsername());
        keyclockUser.setEnabled(true);
        keyclockUser.setEmailVerified(true);

        if(person.getPassword()!=null){
            CredentialRepresentation credentialRepresentation=new CredentialRepresentation();
            credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
            credentialRepresentation.setValue(person.getPassword());
            credentialRepresentation.setTemporary(false);

            keyclockUser.setCredentials(Collections.singletonList(credentialRepresentation));

        }

        Response response=keycloak.realm(realm).users().create(keyclockUser);

        if(response.getStatus()!= StatusCode.CREATED){

            return CustomResponse.getBuilder()
                    .message("Unable to create User!")
                    .statusCode(response.getStatus())
                    .build();
        }

        UserRepresentation userRepresentation=keycloak.realm(realm).users().searchByEmail(person.getEmail(),true).get(0);

        return CustomResponse
                .getBuilder()
                .message("User created successfully.")
                .statusCode(StatusCode.CREATED)
                .data(userRepresentation)
                .build();
    }
    public CustomResponse getAllUsers() {
        try{
            List<UserRepresentation>userRepresentationList=keycloak.realm(realm).users().list();

            String message=null;

            if(userRepresentationList.isEmpty()){
                message="No User found!";
            }

            return CustomResponse.getBuilder()
                    .message(message)
                    .statusCode(StatusCode.OK)
                    .data(userRepresentationList)
                    .build();
        }
        catch (Exception e){
              return CustomResponse
                      .getBuilder()
                      .message("Unknown errors has occured!")
                      .errorMessages(e.getMessage())
                      .data(new ArrayList<UserRepresentation>())
                      .build();
        }

    }
    public CustomResponse getUserById(String userId){

        UserResource userResource=keycloak.realm(realm).users().get(userId);

        try{
            UserRepresentation userRepresentation=userResource.toRepresentation();

            return CustomResponse.getBuilder()
                    .statusCode(StatusCode.OK)
                    .data(userRepresentation)
                    .build();
        }
        catch(Exception e){
              throw new DataNotFoundException("User",e.getMessage());
        }
    }
    public CustomResponse getUserByFieldName(String val,String fieldName){
        List<UserRepresentation> userRepresentationList;

        if(fieldName.equals("email")){
            userRepresentationList=keycloak.realm(realm).users().searchByEmail(val,true);
        }
        else if(fieldName.equals("firstName")){
            userRepresentationList=keycloak.realm(realm).users().searchByFirstName(val,true);
        }
        else if(fieldName.equals("lastName")){
            userRepresentationList=keycloak.realm(realm).users().searchByLastName(val,true);
        }
        else{
            userRepresentationList=keycloak.realm(realm).users().searchByUsername(val,true);
        }

        String message=null;
        if(userRepresentationList.isEmpty()){
            message="No user found with the given "+fieldName+"!";
        }
        return CustomResponse.getBuilder()
                .statusCode(StatusCode.OK)
                .message(message).data(userRepresentationList)
                .build();
    }
    public CustomResponse deleteUser(String userId){
        Response response=keycloak.realm(realm).users().delete(userId);

        String message=null;

        if(response.getStatus()==StatusCode.NOT_FOUND){
            message="User with given id does not exists!";
        }
        else if(response.getStatus()!=StatusCode.NO_CONTENT){
            message="Unable to delete User!";
        }

        return CustomResponse.getBuilder()
                .message(message)
                .statusCode(response.getStatus())
                .build();

    }
    public CustomResponse updateUser(String userId, Person updatedPerson){

        UserResource userResource=keycloak.realm(realm).users().get(userId);

        try{
            UserRepresentation existingPerson=userResource.toRepresentation();


            if(updatedPerson.getUsername()!=null){
                existingPerson.setUsername(updatedPerson.getUsername());
            }
            if(updatedPerson.getEmail()!=null){
                existingPerson.setEmail(updatedPerson.getEmail());
            }
            if(updatedPerson.getFirstName()!=null){
                existingPerson.setFirstName(updatedPerson.getFirstName());
            }
            if(updatedPerson.getLastName()!=null){
                existingPerson.setLastName(updatedPerson.getLastName());
            }
            if(updatedPerson.getEnabled()!=null){
                boolean isEnabled=updatedPerson.getEnabled().equalsIgnoreCase("true")?true:false;
                existingPerson.setEnabled(isEnabled);
            }
            if(updatedPerson.getEmailVerified()!=null){
                boolean isEmailVerified=updatedPerson.getEmailVerified().equalsIgnoreCase("true")?true:false;
                existingPerson.setEmailVerified(isEmailVerified);
            }
            userResource.update(existingPerson);

            existingPerson=keycloak.realm(realm).users().get(userId).toRepresentation();

            return CustomResponse.getBuilder()
                    .message("User successfully updated")
                    .statusCode(StatusCode.OK)
                    .data(existingPerson)
                    .build();
        }
        catch (Exception e){
            throw new DataNotFoundException("User",e.getMessage());
        }
    }
}