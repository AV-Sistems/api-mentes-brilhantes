package br.com.avsistems.resource;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static io.restassured.RestAssured.given;

@QuarkusTest
class AdminResourcesTest {

    @Test
    void testAdminEndpointsWithoutAuth() {
        given()
                .when()
                .get("/admin")
                .then()
                .statusCode(401);

        given()
                .when()
                .get("/admin/inactive")
                .then()
                .statusCode(401);

        given()
                .when()
                .put("/admin/alter-active-user/" + UUID.randomUUID())
                .then()
                .statusCode(401);
    }
}

