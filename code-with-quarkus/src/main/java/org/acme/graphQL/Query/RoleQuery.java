package org.acme.graphQL.Query;


import io.quarkus.security.Authenticated;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import org.acme.Validations.ValidParam;
import org.acme.graphQL.service.KeycloakRoleService;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Mutation;
import org.eclipse.microprofile.graphql.Query;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.keycloak.representations.idm.RoleRepresentation;

import java.util.List;

@GraphQLApi
public class RoleQuery {

    @Inject
    private KeycloakRoleService keycloakRoleService;

    @Inject
    private JsonWebToken jwt;


    @RolesAllowed("admin")
    @Mutation("createrole")
    public RoleRepresentation createRole(@ValidParam @QueryParam("roleName") String roleName){
         return keycloakRoleService.createRole(roleName);
    }

    @RolesAllowed("admin")
    @Query("allroles")
    public List<RoleRepresentation> getAllRoles(){
        return keycloakRoleService.getAllRoles();
    }

    @RolesAllowed("admin")
    @Mutation("assignroletouser")
    public void assignRoleToUser(
            @ValidParam @QueryParam("id") String id,
            @ValidParam @QueryParam("roleName") String roleName){

        keycloakRoleService
                .assignRoleToUser(id,roleName);

    }

    @RolesAllowed("admin")
    @Mutation("revokerolefromuser")
    public void revokeRoleFromUser(
            @ValidParam @QueryParam("id") String id,
            @ValidParam @QueryParam("roleName") String roleName){

        keycloakRoleService
                .revokeRoleFromUser(id, roleName);

    }

    @RolesAllowed("admin")
    @Mutation("deleterole")
    public void deleteRole(@ValidParam @QueryParam("roleName") String roleName){
        keycloakRoleService.deleteRole(roleName);
    }

    @Query("userroles")
    @Authenticated
    public  List<RoleRepresentation> getUserRoles(){
        String userId=jwt.getSubject();
        return keycloakRoleService.getUserRoles(userId);
    }
}

