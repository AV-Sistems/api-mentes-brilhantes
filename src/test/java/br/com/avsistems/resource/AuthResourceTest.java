package br.com.avsistems.resource;

import br.com.avsistems.dto.response.UserResponseDto;
import br.com.avsistems.service.UserService;
import br.com.avsistems.type.UserType;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@QuarkusTest
class AuthResourceTest {

    @InjectMock
    UserService userService;

    @Test
    @Disabled("Credencial seed instavel no ambiente de teste atual")
    void testLoginSuccessWithSeedUser() {
        given()
                .contentType("application/json")
                .body("{\"email\":\"usuario01@mentes.com\",\"password\":\"password\"}")
                .when()
                .post("/auth/login")
                .then()
                .statusCode(200)
                .body("token", notNullValue())
                .body("user.email", equalTo("usuario01@mentes.com"));
    }

    @Test
    void testLoginInvalidCredentials() {
        given()
                .contentType("application/json")
                .body("{\"email\":\"usuario01@mentes.com\",\"password\":\"senha-errada\"}")
                .when()
                .post("/auth/login")
                .then()
                .statusCode(401)
                .body("message", containsString("incorretos"));
    }

    @Test
    void testRegisterSuccess() {
        UserResponseDto responseDto = new UserResponseDto(
                UUID.randomUUID(),
                "Novo Usuario",
                "novo@teste.com",
                UserType.USER,
                0,
                0,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );

        when(userService.createUser(any())).thenReturn(responseDto);

        given()
                .contentType("application/json")
                .body("{\"name\":\"Novo Usuario\",\"email\":\"novo@teste.com\",\"password\":\"123456\"}")
                .when()
                .post("/auth/register")
                .then()
                .statusCode(201)
                .body("email", equalTo("novo@teste.com"));

        verify(userService, times(1)).createUser(any());
    }
}
