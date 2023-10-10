package org.acme.routes;

import jakarta.enterprise.context.ApplicationScoped;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;



@ApplicationScoped
public class KeycloakRoleRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {

        restConfiguration()
                .component("platform-http")
                .bindingMode(RestBindingMode.json);



        rest("/role")
                .get("/admin/getallroles")
                     .to("direct:getAllRoles")

                .post("/admin/createrole")
                        .param().name("roleName").dataType("string").endParam()
                        .to("direct:createRole")

                .put("/admin/assignroletouser")
                        .param().name("id").dataType("string").endParam()
                        .param().name("roleName").dataType("string").endParam()
                        .to("direct:assignRoleToUser")

                .put("/admin/revokerolefromuser")
                        .param().name("id").dataType("string").endParam()
                        .param().name("roleName").dataType("string").endParam()
                        .to("direct:revokeRoleFromUser")

                .delete("/admin/deleterole")
                        .param().name("roleName").dataType("string").endParam()
                        .to("direct:deleteRole")

                .get("/getuserrole")
                        .param().name("id").dataType("string").endParam()
                        .to("direct:getUserRole");



        from("direct:createRole")
                .to("bean:keycloakRoleService?method=createRole(${header.roleName})")
                .end();

        from("direct:assignRoleToUser")
                .to("bean:keycloakRoleService?method=assignRoleToUser(${header.id},${header.roleName})")
                .end();

        from("direct:revokeRoleFromUser")
                .to("bean:keycloakRoleService?method=revokeRoleFromUser(${header.id},${header.roleName})")
                .end();

        from("direct:deleteRole")
                .to("bean:keycloakRoleService?method=deleteRole(${header.roleName})")
                .end();
        from("direct:getAllRoles")
                .to("bean:keycloakRoleService?method=getAllRoles").end();

        from("direct:getUserRole")
                .to("bean:keycloakRoleService?method=getUserRole(${header.id})")
                .end();
    }
}
