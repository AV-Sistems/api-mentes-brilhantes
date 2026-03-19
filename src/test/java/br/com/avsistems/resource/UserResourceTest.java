package br.com.avsistems.resource;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

@QuarkusTest
public class UserResourceTest {

    // 1. TESTE: CRIAR USUÁRIO (CAMINHO FELIZ)
    @Test
    public void testCreateUserEndpoint() {
        String uniqueEmail = "sucesso" + System.currentTimeMillis() + "@teste.com";
        String body = """
                {
                  "name": "Antonio Teste",
                  "email": "%s",
                  "password": "Senha123",
                  "userType": "ADMIN"
                }
                """.formatted(uniqueEmail);

        given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/users")
                .then()
                .statusCode(201)
                .body("name", is("Antonio Teste"))
                .body("id", notNullValue());
    }

    // 2. TESTE: CRIAR USUÁRIO JÁ EXISTENTE (ERRO 400)
    @Test
    public void testCreateDuplicateUserShouldFail() {
        String emailDuplicado = "duplicado" + System.currentTimeMillis() + "@teste.com";
        String body = """
                {
                  "name": "Antonio",
                  "email": "%s",
                  "password": "123",
                  "userType": "USER"
                }
                """.formatted(emailDuplicado);

        // Cria o primeiro
        given().contentType(ContentType.JSON).body(body).post("/users").then().statusCode(201);

        // Tenta criar o segundo com o mesmo e-mail
        given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/users")
                .then()
                .log().all() //mostra o json no console
                .statusCode(400) // Verifica se o ExceptionMapper capturou o erro
                .body("message", is("Email já cadastrado."));
    }

    // 3. TESTE: BUSCAR POR ID EXISTENTE (CAMINHO FELIZ)
    @Test
    public void testFindByIdSuccess() {
        String body = """
                {
                  "name": "Busca Teste",
                  "email": "busca@teste.com",
                  "password": "123",
                  "userType": "USER"
                }
                """;

        // Primeiro criamos o usuário e pegamos o ID dele da resposta
        String userId = given()
                .contentType(ContentType.JSON)
                .body(body)
                .post("/users")
                .then()
                .statusCode(201)
                .extract().path("id");

        org.junit.jupiter.api.Assertions.assertNotNull(userId, "O ID retornado pela API não deveria ser nulo!");

        // Agora buscamos por esse ID
        given()
                .pathParam("id", userId)
                .when()
                .get("/users/{id}")
                .then()
                .statusCode(200)
                .body("id", is(userId))
                .body("name", is("Busca Teste"));
    }

    // 4. TESTE: BUSCAR POR ID INEXISTENTE (ERRO 404)
    @Test
    public void testFindByIdNotFound() {
        UUID randomId = UUID.randomUUID();

        given()
                .when()
                .get("/users/" + randomId)
                .then()
                .statusCode(404) // Verifica o 404 do ExceptionMapper
                .body("message", is("Usuário não encontrado."));
    }

    //Teste para buscar por email (sucesso)
    @Test
    public void testFindByEmailSuccess() {
        String uniqueEmail = "teste" + System.currentTimeMillis() + "@email.com";
        String body = """
                {
                  "name": "Busca Teste",
                  "email": "%s",
                  "password": "123",
                  "userType": "USER"
                }
                """.formatted(uniqueEmail);

        given()
                .contentType(ContentType.JSON)
                .body(body)
                .post("/users")
                .then()
                .statusCode(201);

        given().when()
                .get("/users/email/" + uniqueEmail)
                .then()
                .statusCode(200)
                .body("name", is("Busca Teste"));
    }

    @Test
    public void testFindByEmailNotFound() {
        String email = "teste"+System.currentTimeMillis()+"@email.com";

        given().when()
                .get("/users/email/" + email)
                .then()
                .statusCode(404)
                .body("message", is("Usuário não encontrado."));
    }

    @Test
    public void testUpdateUserSuccess(){
        String email = "success"+System.currentTimeMillis()+"@email.com";
        String body = """
                {
                  "name": "Antonio",
                  "email": "%s",
                  "password": "123",
                  "userType": "USER"
                }
                """.formatted(email);

        given()
                .contentType(ContentType.JSON)
                .body(body)
                .post("/users")
                .then()
                .statusCode(201);
    }

    @Test
    public void testUpdateUserNotFound(){
        String email = "notfound"+System.currentTimeMillis()+"@email.com";
        String body = """
                {
                  "name": "Antonio",
                  "email": "%s",
                  "userType": "USER"
                }
                """.formatted(email);

        given()
                .contentType(ContentType.JSON)
                .body(body)
                .pathParam("id", UUID.randomUUID())
                .when()
                .put("/users/{id}")
                .then()
                .statusCode(404)
                .body("message", is("Usuário não encontrado."));
    }

    @Test
    public void testDeleteUserSuccess(){
        String email = "success"+System.currentTimeMillis()+"@email.com";
        String body = """
                {
                  "name": "Antonio",
                  "email": "%s",
                  "password": "123",
                  "userType": "USER"
                }
                """.formatted(email);
        String id = given()
                .contentType(ContentType.JSON)
                .body(body)
                .post("/users")
                .then()
                .statusCode(201)
                .extract().path("id");

        given()
                .contentType(ContentType.JSON)
                .pathParam("id", id)
                .when()
                .delete("/users/{id}")
                .then()
                .statusCode(204);
    }
@Test
    public void testDeleteUserNotFound(){
        given()
                .contentType(ContentType.JSON)
                .pathParam("id", UUID.randomUUID())
                .when()
                .delete("/users/{id}")
                .then()
                .statusCode(404)
                .body("message", is("Usuário não encontrado."));
    }
}