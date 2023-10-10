package org.acme.resource;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.acme.Constant.StatusCode;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class KeycloakRoleResourceTest {

    private static final String ADMIN_TOKEN = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJtbk5RczVwVW9rY2cweG03MVFNdDFJdi1VUkZSMHVJVzVtZmdGQ0RrZ2NvIn0.eyJleHAiOjE2OTYzMzU0NzIsImlhdCI6MTY5NjMzNDg3MiwianRpIjoiYjM1N2JkZjctNjY0YS00MTRlLWI0ZmQtODVjMDQyNDlmODU1IiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDgxL3JlYWxtcy90ZXN0RGV2IiwiYXVkIjpbInRlc3RBcHAiLCJhY2NvdW50Il0sInN1YiI6ImE1NjhmNmUxLTk4NjEtNDU0MC04Njc2LTRmYWRiZjVhM2MwNSIsInR5cCI6IkJlYXJlciIsImF6cCI6InJlYWN0QXBwIiwic2Vzc2lvbl9zdGF0ZSI6IjhjZjQxZmY2LTc1ZDQtNGI2OC1hZmNhLTc3NDkyZDFhODg1YiIsImFjciI6IjEiLCJhbGxvd2VkLW9yaWdpbnMiOlsiKiJdLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsiZGVmYXVsdC1yb2xlcy10ZXN0ZGV2Iiwib2ZmbGluZV9hY2Nlc3MiLCJhZG1pbiIsInVtYV9hdXRob3JpemF0aW9uIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsidGVzdEFwcCI6eyJyb2xlcyI6WyJhZG1pbiJdfSwiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJvcGVuaWQgZW1haWwgcHJvZmlsZSIsInNpZCI6IjhjZjQxZmY2LTc1ZDQtNGI2OC1hZmNhLTc3NDkyZDFhODg1YiIsImVtYWlsX3ZlcmlmaWVkIjp0cnVlLCJuYW1lIjoiYWpheSBrdW0iLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJhamF5MjU2NDEiLCJnaXZlbl9uYW1lIjoiYWpheSIsImZhbWlseV9uYW1lIjoia3VtIiwiZW1haWwiOiJhamF5MjU2NDFAZ21haWwuY29tIn0.RtD8jNgD0Qzr-FgGhV_ftgSkJccK-wFfr8feJalzNJE4pbczRkLEeZhA4B7XhrOl6yp0at2xH4DxcVnHdP4Q0TT4G_gf0v8ckiRxJ4M9UmScccM7q44HPzDZS_eh4ZPLqzXnEgzHrNeaL_CHhiOYvFlF__8uiCKInKAUaaCIVpnQuixx1cx6Se7P5mVKzsjZLbaS58Yx4IMsBDrFE7aLnSBoD2HtGu-iDuLe6uV5javI-jJZFW8fMXIS57HyNnbYFRdNwdbKhF2aZ80vl4NA8sSl_EMHCfoCRHdblcANpuZBWDdX5XMIqsqI1iSOQ6o_IEvj3Klqn4HKZJJ2W4oKLw";

//    @BeforeAll
//    public static void setup() {
//
//    }

    @Test
    @Order(1)
    public void testCreateRole() {
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + ADMIN_TOKEN)
                .queryParam("roleName", "user")
                .when()
                .post("/role/admin/createrole")
                .then()
                .statusCode(StatusCode.CREATED)
                .body("message", equalTo("Role user created Successfully"));
    }

    @Test
    @Order(2)
    public void testGetAllRoles() {
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + ADMIN_TOKEN)
                .when()
                .get("/role/admin/getallroles")
                .then()
                .statusCode(200)
                .body("data.size()", equalTo(5));
    }

    @Test
    @Order(3)
    public void testAssignRoleToUser() {
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + ADMIN_TOKEN)
                .queryParam("id", "879d7421-ea9d-4fe9-b7dc-561ceeeef2e6")
                .queryParam("roleName", "user")
                .when()
                .put("/role/admin/assignroletouser")
                .then()
                .statusCode(200)
                .body("message", equalTo("Role is assigned to the given user."));
    }

    @Test
    @Order(4)
    public void testRevokeRoleFromUser() {
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + ADMIN_TOKEN)
                .queryParam("id", "879d7421-ea9d-4fe9-b7dc-561ceeeef2e6")
                .queryParam("roleName", "user")
                .when()
                .put("/role/admin/revokerolefromuser")
                .then()
                .statusCode(200)
                .body("message", equalTo("Role from given user is successfully revoked"));
    }

    @Test
    @Order(5)
    public void testDeleteRole() {
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + ADMIN_TOKEN)
                .queryParam("roleName", "user")
                .when()
                .delete("/role/admin/deleterole")
                .then()
                .statusCode(StatusCode.NO_CONTENT);

    }

}
