package tests;

import io.restassured.http.ContentType;
import models.lombok.LoginBodyLombockModel;
import models.lombok.LoginResponseLombockModel;
import models.pojo.LoginBodyPOJOModel;
import models.pojo.LoginResponsePOJOModel;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class ReqresTestWithLombok {
    @Test
    void reqresLoginTestWithLombock() {

        LoginBodyLombockModel loginBody = new LoginBodyLombockModel();
        loginBody.setEmail("eve.holt@reqres.in");
        loginBody.setPassword("cityslicka");


        LoginResponseLombockModel response =
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
                        .extract().as(LoginResponseLombockModel.class);
        //assertEquals("QpwL5tke4Pnpja7X4", response.getToken());
        assertThat(response.getToken()).isEqualTo("QpwL5tke4Pnpja7X4");
    }
}
