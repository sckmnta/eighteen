package tests;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.ContentType;
import models.lombok.LoginBodyLombockModel;
import models.lombok.LoginResponseLombockModel;
import org.junit.jupiter.api.Test;

import static helpers.CustomAllureListener.withCustomTemplates;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static specs.LoginSpec.loginRequestSpec;
import static specs.LoginSpec.loginResponseSpec;

public class ReqresTestWithLombok {
    @Test
    void reqresLoginTestWithAllure() {

        LoginBodyLombockModel loginBody = new LoginBodyLombockModel();
        loginBody.setEmail("eve.holt@reqres.in");
        loginBody.setPassword("cityslicka");


        LoginResponseLombockModel response =
                given()
                        .filter(new AllureRestAssured())
                        .log().uri()
                        .body(loginBody)
                        .contentType(ContentType.JSON)
                        .when()
                        .post("https://reqres.in/api/login")
                        .then()
                        .log().status()
                        .log().body()
                        .statusCode(200)
                        .extract().as(LoginResponseLombockModel.class);
        assertThat(response.getToken()).isEqualTo("QpwL5tke4Pnpja7X4");
    }

    @Test
    void reqresLoginTestWithCustomAllureSteps() {

        step("prepare testdata");

        LoginBodyLombockModel loginBody = new LoginBodyLombockModel();
        loginBody.setEmail("eve.holt@reqres.in");
        loginBody.setPassword("cityslicka");

        LoginResponseLombockModel response = step("make request", () ->
                given()
                        .filter(withCustomTemplates())
                        .log().uri()
                        .body(loginBody)
                        .contentType(ContentType.JSON)
                        .when()
                        .post("https://reqres.in/api/login")
                        .then()
                        .log().status()
                        .log().body()
                        .statusCode(200)
                        .extract().as(LoginResponseLombockModel.class));


        step("verify response", () ->
                assertThat(response.getToken())
                        .isEqualTo("QpwL5tke4Pnpja7X4"));
    }

    @Test
    void reqresLoginTestWithSpec() {

        step("prepare testdata");

        LoginBodyLombockModel loginBody = new LoginBodyLombockModel();
        loginBody.setEmail("eve.holt@reqres.in");
        loginBody.setPassword("cityslicka");

        LoginResponseLombockModel response = step("make request", () ->
                given(loginRequestSpec)
                        .body(loginBody)
                        .when()
                        .post()
                        .then()
                        .spec(loginResponseSpec)
                        .extract().as(LoginResponseLombockModel.class));


        step("verify response", () ->
                assertThat(response.getToken())
                        .isEqualTo("QpwL5tke4Pnpja7X4"));
    }
}
