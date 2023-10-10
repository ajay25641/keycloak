package org.acme.graphQL.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.acme.Model.Person;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.Collections;
import java.util.List;

@ApplicationScoped
public class PersonService {

    private String realm="testDev";
    @Inject
    private Keycloak keycloak;

    public UserRepresentation createUser(Person person){

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

        keycloak.realm(realm).users().create(keyclockUser);

        return keycloak.realm(realm).users().searchByUsername(keyclockUser.getUsername(),true).get(0);

//
    }
    public List<UserRepresentation> getAllUsers() {

            return keycloak.realm(realm).users().list();
    }
    public UserRepresentation getUserById(String userId){

        return keycloak.realm(realm).users().get(userId).toRepresentation();
    }
    public List<UserRepresentation> getUserByFieldName(String val,String fieldName){
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

        return userRepresentationList;
    }
    public void deleteUser(String userId){
        keycloak.realm(realm).users().delete(userId);
    }
    public UserRepresentation updateUser(String userId, Person updatedPerson){

        UserResource userResource=keycloak.realm(realm).users().get(userId);

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

        return existingPerson;

    }
}