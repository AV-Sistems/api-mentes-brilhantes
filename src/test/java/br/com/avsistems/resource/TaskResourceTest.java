package br.com.avsistems.resource;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class TaskResourceTest {


    String body = """
                {
                    "name": "Exemple Task",
                    "description": "This is a task example",
                    "tasksPoints": 10
                }
                """;

    @Test
    public void testCreateTaskEndpoint() {
        given().contentType(ContentType.JSON)
                .body(body)
                .post("/task")
                .then()
                .statusCode(201)
                .body("name", is("Exemple Task"));
    }

    @Test
    public void testUpdateTaskEndpoint(){
        String id = given().contentType(ContentType.JSON)
                .body(body)
                .post("/task")
                .then()
                .statusCode(201)
                .extract().path("id");

        String task = """
                    {
                        "name": "Exemple Task Updated",
                        "description": "This is a task example updated",
                        "tasksPoints": 50,
                        "tasksStatus": "DEACTIVE"
                    }
                """;

        given().contentType(ContentType.JSON)
                .body(task)
                .pathParam("id", id)
                .when()
                .put("/task/{id}")
                .then()
                .statusCode(200);
    }

    @Test
    public void testUpdateTaskEndpointFail(){
        given().contentType(ContentType.JSON)
                .body(body)
                .pathParam("id", UUID.randomUUID())
                .when()
                .put("/task/{id}")
                .then()
                .statusCode(404);
    }

    @Test
    public void testDeleteTaskEndpointSuccess(){
        String id = given().contentType(ContentType.JSON)
                .body(body)
                .post("/task")
                .then()
                .statusCode(201)
                .extract().path("id");

        given().contentType(ContentType.JSON)
                .pathParam("id", id)
                .when()
                .delete("/task/{id}")
                .then()
                .statusCode(204);
    }

    @Test
    public void testDeleteTaskEndpointFail(){
        given().contentType(ContentType.JSON)
                .pathParam("id", UUID.randomUUID())
                .when()
                .delete("/task/{id}")
                .then()
                .statusCode(404);
    }

    @Test
    public void testFindAllTasksEndpoint(){
        given().when()
                .get("/task")
                .then()
                .statusCode(200);
    }

    @Test
    public void testFindTaskEndpointByIDSuccess(){
        String id = given().contentType(ContentType.JSON)
                .body(body)
                .post("/task")
                .then()
                .statusCode(201)
                .extract().path("id");

        given().contentType(ContentType.JSON)
                .pathParam("id", id)
                .when()
                .get("/task/{id}")
                .then()
                .statusCode(200);
    }

    @Test
    public void testFindTaskEndpointByIdFail(){
        given().contentType(ContentType.JSON)
                .pathParam("id", UUID.randomUUID())
                .when()
                .get("/task/{id}")
                .then()
                .statusCode(404);
    }

    @Test
    public void testFindTaskEndpointByNameSuccess(){
        String name = given().contentType(ContentType.JSON)
                .body(body)
                .post("/task")
                .then()
                .statusCode(201)
                .extract().path("name");

        given().contentType(ContentType.JSON)
                .pathParam("name", name)
                .when()
                .get("/task/name/{name}")
                .then()
                .statusCode(200);
    }

    @Test
    public void testFindTaskEndpointByNameFail(){
        String name = "Test Fail";
        given().contentType(ContentType.JSON)
                .pathParam("name", name)
                .when()
                .get("/task/name/{name}")
                .then()
                .statusCode(404);
    }
}
