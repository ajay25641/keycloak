package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import org.acme.Constant.StatusCode;
import org.acme.CustomException.DataNotFoundException;
import org.acme.config.PropertyConfig;
import org.acme.responses.CustomResponse;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RoleResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.Collections;
import java.util.List;


@ApplicationScoped
@Named("keycloakRoleService")
@Slf4j
public class KeycloakRoleService {

    @Inject
    private Keycloak keycloak;

    private String realm="testDev";


    public CustomResponse createRole(String roleName){

        RoleResource roleResource=keycloak.realm(realm).roles().get(roleName);

        try{

            roleResource.toRepresentation();

            return CustomResponse
                    .getBuilder()
                    .statusCode(StatusCode.CONFLICT)
                    .message("Role "+roleName+" already exists!")
                    .build();
        }
        catch(Exception e){
            RoleRepresentation roleRepresentation=new RoleRepresentation();
            roleRepresentation.setName(roleName);

            keycloak.realm(realm).roles().create(roleRepresentation);
            roleRepresentation=keycloak.realm(realm).roles().get(roleName).toRepresentation();

            return CustomResponse
                    .getBuilder()
                    .message("Role "+roleName+" created Successfully")
                    .statusCode(StatusCode.CREATED)
                    .data(roleRepresentation)
                    .build();
        }
    }

    public CustomResponse getAllRoles(){
        List<RoleRepresentation> roleRepresentationList=keycloak.realm(realm).roles().list();

        String message=null;

        if(roleRepresentationList.isEmpty()){
            message="Sorry! No role exists.";
        }

        return CustomResponse
                .getBuilder()
                .message(message)
                .statusCode(StatusCode.OK)
                .data(roleRepresentationList)
                .build();
    }

    public CustomResponse assignRoleToUser(String userId, String roleName){
        UserResource userResource=keycloak.realm(realm).users().get(userId);
        RoleResource roleResource=keycloak.realm(realm).roles().get(roleName);

        UserRepresentation userRepresentation;
        RoleRepresentation roleRepresentation;
        try{
            userResource.toRepresentation();
        }
        catch(Exception e){
            throw new DataNotFoundException("User",e.getMessage());
        }

        try{
            roleRepresentation=roleResource.toRepresentation();
        }
        catch (Exception e){
            throw new DataNotFoundException("Role",e.getMessage());
        }

        userResource.roles().realmLevel().add(Collections.singletonList(roleRepresentation));

        userRepresentation=keycloak.realm(realm).users().get(userId).toRepresentation();

        return CustomResponse.getBuilder()
                .statusCode(StatusCode.OK)
                .message("Role is assigned to the given user.")
                .data(userRepresentation).build();
    }

    public CustomResponse revokeRoleFromUser(String userId,String roleName){
        UserResource userResource=keycloak.realm(realm).users().get(userId);

        UserRepresentation userRepresentation;
        RoleRepresentation roleToRemove=null;

        try{
            userResource.toRepresentation();
        }
        catch(Exception e){
            throw new DataNotFoundException("User",e.getMessage());
        }

        for(RoleRepresentation role:userResource.roles().realmLevel().listAll()){
            if(role.getName().equals(roleName)){
                roleToRemove=role;
            }
        }
        if(roleToRemove!=null){
            userResource.roles().realmLevel().remove(Collections.singletonList(roleToRemove));
        }

        userRepresentation=keycloak.realm(realm).users().get(userId).toRepresentation();

        return CustomResponse.getBuilder()
                .message("Role from given user is successfully revoked")
                .statusCode(StatusCode.OK)
                .data(userRepresentation)
                .build();
    }


    public CustomResponse deleteRole(String roleName){
        RoleResource roleResource=keycloak.realm(realm).roles().get(roleName);

        try{
            roleResource.toRepresentation();
            roleResource.remove();

            return CustomResponse
                    .getBuilder()
                    .statusCode(StatusCode.NO_CONTENT)
                    .build();
        }
        catch (Exception e){
            throw new DataNotFoundException("Role",e.getMessage());
        }
    }

    public CustomResponse getUserRole(String userId){


       UserResource userResource= keycloak.realm(realm).users().get(userId);

       try {
           List<RoleRepresentation>roleRepresentationList=userResource.roles().realmLevel().listAll();

           return CustomResponse
                   .getBuilder()
                   .statusCode(StatusCode.OK)
                   .data(roleRepresentationList)
                   .build();


       }
       catch (Exception ex){
           return CustomResponse
                   .getBuilder()
                   .statusCode(StatusCode.NOT_FOUND)
                   .message("User not found!")
                   .errorMessages(ex.getMessage())
                   .build();
       }
    }
}