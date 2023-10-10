package org.acme.graphQL.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RoleResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.RoleRepresentation;
import java.util.Collections;
import java.util.List;


@ApplicationScoped
public class KeycloakRoleService {

    @Inject
    private Keycloak keycloak;

    private String realm="testDev";


    public RoleRepresentation createRole(String roleName){


            RoleRepresentation roleRepresentation=new RoleRepresentation();
            roleRepresentation.setName(roleName);

            keycloak.realm(realm).roles().create(roleRepresentation);
            roleRepresentation=keycloak.realm(realm).roles().get(roleName).toRepresentation();

            return roleRepresentation;

    }

    public List<RoleRepresentation> getAllRoles(){
        List<RoleRepresentation> roleRepresentationList=keycloak.realm(realm).roles().list();

        return roleRepresentationList;
    }

    public void assignRoleToUser(String userId, String roleName){
        UserResource userResource=keycloak.realm(realm).users().get(userId);
        RoleRepresentation roleRepresentation=keycloak.realm(realm).roles().get(roleName).toRepresentation();

        userResource.roles().realmLevel().add(Collections.singletonList(roleRepresentation));


    }

    public void revokeRoleFromUser(String userId,String roleName){
        UserResource userResource=keycloak.realm(realm).users().get(userId);

        RoleRepresentation roleToRemove=null;


        for(RoleRepresentation role:userResource.roles().realmLevel().listAll()){
            if(role.getName().equals(roleName)){
                roleToRemove=role;
            }
        }
        if(roleToRemove!=null){
            userResource.roles().realmLevel().remove(Collections.singletonList(roleToRemove));
        }
    }


    public void deleteRole(String roleName){
        RoleResource roleResource=keycloak.realm(realm).roles().get(roleName);

        roleResource.remove();

    }

    public List<RoleRepresentation> getUserRoles(String userId){
        return keycloak.realm(realm).users().get(userId).roles().realmLevel().listAll();
    }
}
