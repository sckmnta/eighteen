package tests;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

import static org.hamcrest.Matchers.is;

public class ReqresInTests {

     /*
    1. Make request (POST) to https://reqres.in/api/login with body {"email": "eve.holt@reqres.in","password": "cityslicka"}
    2. Get response {"token": "QpwL5tke4Pnpja7X4"}
    3. Check if token equals QpwL5tke4Pnpja7X4
     */


    @Test
    void reqresLoginTest() {

        String body = "{\"email\": \"eve.holt@reqres.in\",\"password\": \"cityslicka\"}";

        given()
                .log().uri()
                .body(body)
                .contentType(ContentType.JSON)
                .when()
                .post("https://reqres.in/api/login")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("token", is("QpwL5tke4Pnpja7X4"));
    }

    @Test
    void reqresUnsuccesfulLoginTestWithoutMail() {

        String body = "{\"password\": \"cityslicka\"}";

        given()
                .log().uri()
                .body(body)
                .contentType(ContentType.JSON)
                .when()
                .post("https://reqres.in/api/login")
                .then()
                .log().status()
                .log().body()
                .statusCode(400)
                .body("error", is("Missing email or username"));
    }

    @Test
    void reqresUnsuccesfulLoginTestWithoutPassword() {

        String body = "{\"email\": \"eve.holt@reqres.in\"}";

        given()
                .log().uri()
                .body(body)
                .contentType(ContentType.JSON)
                .when()
                .post("https://reqres.in/api/login")
                .then()
                .log().status()
                .log().body()
                .statusCode(400)
                .body("error", is("Missing email or username"));
    }

    @Test
    void reqresUnsuccesfulLoginTestWithEmptyData() {

        String body = "{ }";

        given()
                .log().uri()
                .body(body)
                .contentType(ContentType.JSON)
                .when()
                .post("https://reqres.in/api/login")
                .then()
                .log().status()
                .log().body()
                .statusCode(400)
                .body("error", is("Missing email or username"));
    }


}
