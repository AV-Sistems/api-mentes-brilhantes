package br.com.avsistems.resource;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@QuarkusTest
class FileResourceTest {

    private static final String TYPE = "test";
    private static final String FILE = "sample.bin";

    @BeforeEach
    void setUp() throws IOException {
        Path dir = Path.of("uploads", TYPE);
        Files.createDirectories(dir);
        Files.write(dir.resolve(FILE), new byte[]{1, 2, 3, 4});
    }

    @Test
    void testGetFileSuccess() {
        given()
                .when()
                .get("/uploads/" + TYPE + "/" + FILE)
                .then()
                .statusCode(200)
                .header("Content-Type", equalTo("application/octet-stream"));
    }

    @Test
    void testGetFileNotFound() {
        given()
                .when()
                .get("/uploads/" + TYPE + "/missing.bin")
                .then()
                .statusCode(404)
                .body(equalTo(""));
    }
}

