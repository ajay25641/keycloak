package org.acme.resource;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.acme.Model.Person;
import org.junit.jupiter.api.*;

import static org.hamcrest.Matchers.*;

@QuarkusTest
@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PersonQueryTest {

    private static String accessToken="eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJtbk5RczVwVW9rY2cweG03MVFNdDFJdi1VUkZSMHVJVzVtZmdGQ0RrZ2NvIn0.eyJleHAiOjE2OTYzMzU3OTgsImlhdCI6MTY5NjMzNTE5OCwianRpIjoiN2JkOTA4MDEtOWIwNS00NjljLTgwMDctYjI1YmY1ZGNiOWJkIiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDgxL3JlYWxtcy90ZXN0RGV2IiwiYXVkIjpbInRlc3RBcHAiLCJhY2NvdW50Il0sInN1YiI6ImE1NjhmNmUxLTk4NjEtNDU0MC04Njc2LTRmYWRiZjVhM2MwNSIsInR5cCI6IkJlYXJlciIsImF6cCI6InJlYWN0QXBwIiwic2Vzc2lvbl9zdGF0ZSI6IjVkYjMxOTU0LWY3NWUtNDYyMi1hYzc2LTU4ZWQ1ZWU5MzYxNSIsImFjciI6IjEiLCJhbGxvd2VkLW9yaWdpbnMiOlsiKiJdLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsiZGVmYXVsdC1yb2xlcy10ZXN0ZGV2Iiwib2ZmbGluZV9hY2Nlc3MiLCJhZG1pbiIsInVtYV9hdXRob3JpemF0aW9uIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsidGVzdEFwcCI6eyJyb2xlcyI6WyJhZG1pbiJdfSwiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJvcGVuaWQgZW1haWwgcHJvZmlsZSIsInNpZCI6IjVkYjMxOTU0LWY3NWUtNDYyMi1hYzc2LTU4ZWQ1ZWU5MzYxNSIsImVtYWlsX3ZlcmlmaWVkIjp0cnVlLCJuYW1lIjoiYWpheSBrdW0iLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJhamF5MjU2NDEiLCJnaXZlbl9uYW1lIjoiYWpheSIsImZhbWlseV9uYW1lIjoia3VtIiwiZW1haWwiOiJhamF5MjU2NDFAZ21haWwuY29tIn0.qskR_gzrJjAXevLYjq_vTl4QsqHCEOBKSkkFse7GW4kziNU5o2EpF4OGYS3SKY5SJERWs2WSeslpkoL83o3pZtQQcYe4_SijRJoavoKaC_YDCJeFmmoPUhvU-GIB9WpvKt_GyIHmNKFtZOtA6lN42e5BN3FMVQT_RetA3LMF5ptzuc-NPmzlTd6OfxGjDmZ8GwgsH7YDs3VM6AYDDNz_imsTA28Lc2KiMwu6tcZv1GCIq_0n9rloXNgxVmVG8gda3vZc1PWnpnJXh-kseuFebH_Fl93SNnECx6DoBYW2XuuI718UasH7y2j6GMd8AGMENwYPwa3MsUpIexd0hRAwow";


    PersonQueryTest(){
        //accessToken=TokenStore.getAccessToken();
        //log.info("access_token "+accessToken);
    }

    @Test
    @Order(1)
    //@Disabled
    public void testCreateUser() {

        Person person = new Person();
        person.setFirstName("test1");
        person.setLastName("user1");
        person.setEmail("testuser1@gmail.com");
        person.setUsername("test_user1");
        person.setPassword("12345");

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(person)
                .when()
                .post("/user/createuser")
                .then()
                .statusCode(Response.Status.CREATED.getStatusCode())
                .body("data.id", not(emptyOrNullString()));
    }

    @Test
    @Order(2)
    public void testGetAllUser() {


        RestAssured.given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + accessToken)
                .when()
                .get("/user/admin/getallusers")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("data.size()",equalTo(5));
    }

    @Test
    @Order(3)
    public void testGetUserById() {



        RestAssured.given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + accessToken)
                .when()
                .get("/user/getuserbyid")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("data.id",not(emptyOrNullString()));
    }

    @Test
    @Order(4)
    public void testGetUserByFieldName() {
        // Implement test for the getUserByFieldName endpoint with authorization
        //String accessToken = generateAccessToken();

        RestAssured.given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + accessToken)
                .queryParam("email", "ajay25641@gmail.com")
                .when()
                .get("/user/admin/getuserbyfieldname")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("data.size()",equalTo(1));
    }

    @Test
    @Order(5)
    public void testUpdateUser() {



        Person personToUpdate = new Person();
        //personToUpdate.setId("a568f6e1-9861-4540-8676-4fadbf5a3c05");
        personToUpdate.setLastName("kumar");

        RestAssured.given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + accessToken)
                .body(personToUpdate)
                .when()
                .put("/user/updateuser")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("data.id",not(emptyOrNullString()));
    }

    @Test
    @Order(6)
    public void testDeleteUser() {


        RestAssured.given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + accessToken)
                .queryParam("id", "879d7421-ea9d-4fe9-b7dc-561ceeeef2e6")
                .when()
                .delete("/user/admin/deleteuser")
                .then()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode());
    }


}
