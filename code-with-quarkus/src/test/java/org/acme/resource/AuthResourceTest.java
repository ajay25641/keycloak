package org.acme.resource;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.acme.Dto.LoginDto;
import org.junit.jupiter.api.*;


@QuarkusTest
@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthResourceTest {

    private static String accessToken="";
    private static String refreshToken="";




    public String getRefreshToken(){
        return refreshToken;
    }
    public String getAccessToken(){
        return accessToken;
    }

    @Test
    @Order(1)
    void login() {
        LoginDto loginDto=new LoginDto();
        loginDto.setUsername("ajay25641");
        loginDto.setPassword("12345");

        Response response=RestAssured
                            .given()
                            .contentType(ContentType.JSON)
                            .body(loginDto)
                            .when()
                            .post("/auth/login");
        accessToken=response.jsonPath().get("data.access_token");
        refreshToken=response.jsonPath().get("data.refresh_token");

        response.then().assertThat().statusCode(200);

    }

    @Test
    @Order(2)
    void refresh_token(){
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .queryParam("refresh_token",refreshToken)
                .when()
                .post("/auth/refreshtoken")
                .then().statusCode(200);
    }

    @Test()
    @Order(3)
    void logout() {


        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .queryParam("refresh_token",refreshToken)
                .when()
                .post("/auth/logout")
                .then().statusCode(200);
    }
}