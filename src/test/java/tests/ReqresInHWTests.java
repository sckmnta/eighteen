package tests;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.ContentType;
import models.lombok.PatchBodyLombokModel;
import models.lombok.RegisterBodyLombockModel;
import models.lombok.RegisterResponseLombokModel;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import specs.JSONSpec;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.assertj.core.api.Assertions.assertThat;
import static specs.DeleteSpec.deleteRequestSpec;
import static specs.DeleteSpec.deleteResponseSpec;
import static specs.JSONSpec.jsonResponseSpec;
import static specs.PatchSpec.patchRequestSpec;
import static specs.PatchSpec.patchResponseSpec;
import static specs.RegisterSpec.registerRequestSpec;
import static specs.RegisterSpec.registerResponseSpec;


public class ReqresInHWTests {


    @Test
    @Tag("HW")
    void checkResponseStatus() {
        RegisterBodyLombockModel registerBody = new RegisterBodyLombockModel();
        registerBody.setStatusCode("404");

        RegisterResponseLombokModel response = step("make request", () ->
                given(registerRequestSpec)
                        .when()
                        .get("api/unknown/23")
                        .then()
                        .spec(registerResponseSpec)
                        .statusCode(404))
                .extract().as(RegisterResponseLombokModel.class);
        step("verify no response", () ->
                assertThat(response.getStatusCode())
                        .isEqualTo(0));

    }

    @Test
    @Tag("HW")
    void checkUserListJSONSchema() {
        step("make response and verify if JSON schema equals expected", () ->
                given(registerRequestSpec)
                        .when()
                        .get("api/unknown")
                        .then()
                        .spec(jsonResponseSpec)
                        .body(matchesJsonSchemaInClasspath
                                ("schemes/userlist-scheme-response.json")));
    }

    @Test
    void successfulPatchTest() {

        step("prepare test data");
        PatchBodyLombokModel loginBody = new PatchBodyLombokModel();
        loginBody.setUsername("morpheus");
        loginBody.setJob("zion resident");
        RegisterResponseLombokModel response = step("make request", () ->
                given(patchRequestSpec)
                        .when()
                        .patch("api/users/2")
                        .then()
                        .spec(patchResponseSpec)
                        .extract().as(RegisterResponseLombokModel.class));
        step("verify job", () ->
                assertThat(response.getJob()).isEqualTo("zion resident"));
        step("verify name", () ->
                assertThat(response.getUsername()).isEqualTo("morpheus"));


    }

    @Test
    @Tag("HW")
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
    @Tag("HW")
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
    @Tag("HW")
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
    @Tag("HW")
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
