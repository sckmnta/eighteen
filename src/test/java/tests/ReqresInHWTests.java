package tests;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;


public class ReqresInHWTests {


    @Test
    void checkResponseStatus() {
        given()
                .log().uri()
                .when()
                .get("https://reqres.in/api/unknown/23")
                .then()
                .log().status()
                .log().body()
                .statusCode(404);

    }

    @Test
    void checkUserListJSONSchema() {
        given()
                .log().uri()
                .when()
                .get("https://reqres.in/api/unknown")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemes/userlist-scheme-response.json"));
    }

    @Test
    void successfulPatchTest() {
        String body = "{\"name\": \"morpheus\",\"job\": \"zion resident\"}";
        given()
                .log().uri()
                .body(body)
                .contentType(ContentType.JSON)
                .when()
                .patch("https://reqres.in/api/users/2")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("updatedAt", hasLength(24)); //todo по образу и подобию https://stackoverflow.com/questions/50710872/assert-restassured-response-body-with-regex
    }

    @Test
    void successfulDeleteTest() {
        given()
                .log().uri()
                .contentType(ContentType.JSON)
                .when()
                .delete("https://reqres.in/api/users/2")
                .then()
                .log().status()
                .log().body()
                .statusCode(204);

    }

    @Test
    void successfulRegistrationTest() {
        String body = "{\"email\": \"eve.holt@reqres.in\",\"password\": \"pistol\"}";
        given()
                .log().uri()
                .body(body)
                .contentType(ContentType.JSON)
                .when()
                .post("https://reqres.in/api/register")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("token", is("QpwL5tke4Pnpja7X4"));
    }

    @Test
    void unsuccessfulRegistrationTestWithoutPassword() {
        String body = "{\"email\": \"sydney@fife\"}";
        given()
                .log().uri()
                .body(body)
                .contentType(ContentType.JSON)
                .when()
                .post("https://reqres.in/api/register")
                .then()
                .log().status()
                .log().body()
                .statusCode(400)
                .body("error", is("Missing password"));
    }

    @Test
    void unsuccessfulRegistrationTestWithoutMail() {
        String body = "{\"password\": \"pistol\"}";
        given()
                .log().uri()
                .body(body)
                .contentType(ContentType.JSON)
                .when()
                .post("https://reqres.in/api/register")
                .then()
                .log().status()
                .log().body()
                .statusCode(400)
                .body("error", is("Missing email or username"));
    }


}
