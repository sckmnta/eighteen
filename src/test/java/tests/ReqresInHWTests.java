package tests;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.ContentType;
import models.lombok.RegisterBodyLombockModel;
import models.lombok.RegisterResponseLombokModel;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.assertj.core.api.Assertions.assertThat;
import static specs.DeleteSpec.deleteRequestSpec;
import static specs.DeleteSpec.deleteResponseSpec;
import static specs.RegisterSpec.registerRequestSpec;
import static specs.RegisterSpec.registerResponseSpec;


public class ReqresInHWTests {


    @Test
    void checkResponseStatus() {
        RegisterBodyLombockModel registerBody = new RegisterBodyLombockModel();
        registerBody.setStatusCode("404");

        RegisterResponseLombokModel response = step("make request", () ->
                given()
                        .filter(new AllureRestAssured())
                        .log().uri()
                        .when()
                        .get("https://reqres.in/api/unknown/23")
                        .then()
                        .log().status()
                        .log().body()
                        .statusCode(404))
                .extract().as(RegisterResponseLombokModel.class);
        step("verify no response", () ->
                assertThat(response.getStatusCode())
                        .isEqualTo(0));

    }

    @Test
    void checkUserListJSONSchema() {
        step("make response and verify if JSON schema equals expected", () ->
                given()
                        .filter(new AllureRestAssured())
                        .log().uri()
                        .when()
                        .get("https://reqres.in/api/unknown")
                        .then()
                        .log().status()
                        .log().body()
                        .statusCode(200)
                        .body(matchesJsonSchemaInClasspath
                                ("schemes/userlist-scheme-response.json")));
    }

    @Test
    void successfulPatchTest() {

        step("prepare test data");
        RegisterBodyLombockModel loginBody = new RegisterBodyLombockModel();
        loginBody.setUsername("morpheus");
        loginBody.setJob("zion resident");
        RegisterResponseLombokModel response = step("make request", () ->
                given(registerRequestSpec)
                        .filter(new AllureRestAssured())
                        .log().uri()
                        .body(loginBody)
                        .contentType(ContentType.JSON)
                        .when()
                        .patch("https://reqres.in/api/users/2")
                        .then()
                        .log().status()
                        .log().body()
                        .statusCode(200)
                        .extract().as(RegisterResponseLombokModel.class));
        step("verify job", () ->
                assertThat(response.getJob()).isEqualTo("zion resident"));
        step("verify name", () ->
                assertThat(response.getUsername()).isEqualTo("morpheus"));


    }

    @Test
    void successfulDeleteTest() {
        RegisterBodyLombockModel registerBody = new RegisterBodyLombockModel();
        registerBody.setStatusCode("204");
        step("make request and get status 204", () ->
                given(deleteRequestSpec)
                        .when()
                        .delete("https://reqres.in/api/users/2")
                        .then()
                        .spec(deleteResponseSpec)
                        .statusCode(204));

    }


    @Test
    void successfulRegistrationTest() {
        step("prepare testdata");
        RegisterBodyLombockModel registerBody = new RegisterBodyLombockModel();
        registerBody.setEmail("eve.holt@reqres.in");
        registerBody.setPassword("pistol");
        RegisterResponseLombokModel response = step("make request", () ->
                given(registerRequestSpec)
                        .body(registerBody)
                        .when()
                        .post("/api/register")
                        .then()
                        .spec(registerResponseSpec)
                        .extract().as(RegisterResponseLombokModel.class)
        );
        step("verify response token", () ->
                assertThat(response.getToken()).isEqualTo("QpwL5tke4Pnpja7X4"));
        step("verify response token", () ->
                assertThat(response.getId()).isEqualTo(4));

    }

    @Test
    void unsuccessfulRegistrationTestWithoutPassword() {
        RegisterBodyLombockModel registerBody = new RegisterBodyLombockModel();
        registerBody.setEmail("sydney@fife");
        RegisterResponseLombokModel response = step("make request", () ->
                given(registerRequestSpec)
                        .body(registerBody)
                        .when()
                        .post("/api/register")
                        .then()
                        .statusCode(400)
                        .spec(registerResponseSpec)
                        .extract().as(RegisterResponseLombokModel.class)
        );
        step("verify response", () ->
                assertThat(response.getError()).isEqualTo("Missing password"));
    }

    @Test
    void unsuccessfulRegistrationTestWithoutMail() {
        RegisterBodyLombockModel registerBody = new RegisterBodyLombockModel();
        registerBody.setPassword("pistol");
        RegisterResponseLombokModel response = step("make request", () ->

                given(registerRequestSpec)
                        .body(registerBody)
                        .when()
                        .post("/api/register")
                        .then()
                        .statusCode(400)
                        .spec(registerResponseSpec)
                        .extract().as(RegisterResponseLombokModel.class)
        );
        step("verify response", () ->
                assertThat(response.getError()).isEqualTo("Missing email or username"));

    }


}
