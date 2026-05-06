package br.com.avsistems;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@QuarkusTest
class HelloAndPreflightTest {

    @Test
    void testHelloEndpoint() {
        given()
                .when()
                .get("/hello")
                .then()
                .statusCode(200)
                .body(equalTo("Api Funcionando"));
    }

    @Test
    void testPreflightEndpoint() {
        given()
                .when()
                .options("/_preflight/")
                .then()
                .statusCode(200)
                .body(equalTo(""));
    }
}

