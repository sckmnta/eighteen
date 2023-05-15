package tests;

import io.restassured.http.ContentType;
import models.pojo.LoginBodyModel;
import models.pojo.LoginResponseModel;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class ReqresTestWithLombok {
    @Test
    void reqresLoginTestWithLombock() {

        LoginBodyModel loginBody = new LoginBodyModel();
        loginBody.setEmail("eve.holt@reqres.in");
        loginBody.setPassword("cityslicka");


        LoginResponseModel response =
                given()
                        .log().uri()
                        .body(loginBody)
                        .contentType(ContentType.JSON)
                        .when()
                        .post("https://reqres.in/api/login")
                        .then()
                        .log().status()
                        .log().body()
                        .statusCode(200)
                        .extract().as(LoginResponseModel.class);
        //assertEquals("QpwL5tke4Pnpja7X4", response.getToken());
        assertThat(response.getToken()).isEqualTo("QpwL5tke4Pnpja7X4");
    }
}
