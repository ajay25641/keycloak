package org.acme.resource;


import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.acme.Validations.ValidParam;
import org.acme.service.KeycloakRoleService;

@Path("/role")
public class KeycloakRoleResource {

    @Inject
    private KeycloakRoleService keycloakRoleService;

    @POST
    @Path("/admin/createrole")
    @RolesAllowed("admin")
    public Response createRole(@ValidParam @QueryParam("roleName") String roleName){

        return keycloakRoleService
                .createRole(roleName)
                .responseBuilder();
    }

    @GET
    @Path("/admin/getallroles")
    @RolesAllowed("admin")
    public Response getAllRoles(){
        return keycloakRoleService
                .getAllRoles()
                .responseBuilder();
    }

    @PUT
    @Path("/admin/assignroletouser")
    @RolesAllowed("admin")
    public Response assignRoleToUser(
            @ValidParam @QueryParam("id") String id,
            @ValidParam @QueryParam("roleName") String roleName){

        return keycloakRoleService
                .assignRoleToUser(id,roleName)
                .responseBuilder();
    }

    @PUT
    @Path("/admin/revokerolefromuser")
    @RolesAllowed("admin")
    public Response revokeRoleFromUser(
            @ValidParam @QueryParam("id") String id,
            @ValidParam @QueryParam("roleName") String roleName){

        return keycloakRoleService
                .revokeRoleFromUser(id, roleName)
                .responseBuilder();
    }

    @DELETE
    @Path("/admin/deleterole")
    @RolesAllowed("admin")
    public Response deleteRole(@ValidParam @QueryParam("roleName") String roleName){
        return keycloakRoleService
                .deleteRole(roleName)
                .responseBuilder();
    }
}
