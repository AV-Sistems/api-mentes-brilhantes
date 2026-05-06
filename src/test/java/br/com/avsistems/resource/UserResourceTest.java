package br.com.avsistems.resource;

import br.com.avsistems.dto.response.UserResponseDto;
import br.com.avsistems.service.UserService;
import br.com.avsistems.type.UserType;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@QuarkusTest
@DisplayName("UserResource - Testes de endpoints existentes")
public class UserResourceTest {

    @InjectMock
    UserService userService;

    private UserResponseDto user;

    @BeforeEach
    void setup() {
        user = new UserResponseDto(
                UUID.randomUUID(),
                "Antonio",
                "antonio@teste.com",
                UserType.USER,
                10,
                10,
                "uploads/users/test.png",
                "Universidade X",
                null,
                null,
                null,
                null,
                "Rua A",
                "10",
                "Centro",
                "Sao Paulo",
                "SP",
                "Casa",
                "00000-000"
        );
    }

    // Endpoints publicos

    @Test
    void testFindAllUsers() {
        when(userService.findAllActiveUser()).thenReturn(List.of(user));

        given()
                .when()
                .get("/users")
                .then()
                .statusCode(200)
                .body("$", hasSize(1))
                .body("[0].name", is("Antonio"));

        verify(userService, times(1)).findAllActiveUser();
    }

    @Test
    void testFindRanking() {
        when(userService.findRankingByTotalPoints()).thenReturn(List.of(user));

        given()
                .when()
                .get("/users/ranking")
                .then()
                .statusCode(200)
                .body("$", hasSize(1))
                .body("[0].totalPoints", is(10));

        verify(userService, times(1)).findRankingByTotalPoints();
    }

    @Test
    void testFindByStateAndCity() {
        when(userService.findByStateAndCity("SP", "Sao Paulo")).thenReturn(List.of(user));

        given()
                .when()
                .get("/users/SP/Sao Paulo")
                .then()
                .statusCode(200)
                .body("$", hasSize(1));

        verify(userService, times(1)).findByStateAndCity("SP", "Sao Paulo");
    }

    @Test
    void testFindByName() {
        when(userService.findByNameContaining("Ant")).thenReturn(List.of(user));

        given()
                .when()
                .get("/users/name/Ant")
                .then()
                .statusCode(200)
                .body("$", hasSize(1));

        verify(userService, times(1)).findByNameContaining("Ant");
    }

    @Test
    void testFindByEducationInstituition() {
        when(userService.findByEducationInstituitionContaining("Universidade")).thenReturn(List.of(user));

        given()
                .when()
                .get("/users/education-instituition/Universidade")
                .then()
                .statusCode(200);

        verify(userService, times(1)).findByEducationInstituitionContaining("Universidade");
    }

    @Test
    void testFindByUserType() {
        when(userService.findByUserTypeContaining("USER")).thenReturn(List.of(user));

        given()
                .when()
                .get("/users/user-type/USER")
                .then()
                .statusCode(200);

        verify(userService, times(1)).findByUserTypeContaining("USER");
    }

    @Test
    void testFindByMentesEdition() {
        when(userService.findByMentesEditionContaining("2026")).thenReturn(List.of(user));

        given()
                .when()
                .get("/users/mentes-edition/2026")
                .then()
                .statusCode(200);

        verify(userService, times(1)).findByMentesEditionContaining("2026");
    }

    @Test
    void testFindByDateOfBirth() {
        when(userService.findByDateOfBirth(any())).thenReturn(List.of(user));

        given()
                .when()
                .get("/users/date-of-birth/1990-01-01")
                .then()
                .statusCode(200);

        verify(userService, times(1)).findByDateOfBirth(any());
    }

    @Test
    void testFindInactiveUsers() {
        when(userService.findAllInactiveUser()).thenReturn(List.of(user));

        given()
                .when()
                .get("/users/inactive-user")
                .then()
                .statusCode(200)
                .body("$", hasSize(1));

        verify(userService, times(1)).findAllInactiveUser();
    }

    // Endpoints autenticados (sem token -> 401)

    @Test
    void testUpdateAddressWithoutAuth() {
        given()
                .contentType("application/json")
                .body("{}")
                .when()
                .put("/users/adrress/" + UUID.randomUUID())
                .then()
                .statusCode(401);
    }

    @Test
    void testUpdateUserWithoutAuth() {
        given()
                .contentType("application/json")
                .body("{}")
                .when()
                .put("/users/" + UUID.randomUUID())
                .then()
                .statusCode(401);
    }

    @Test
    void testAlterActiveUserWithoutAuth() {
        given()
                .when()
                .put("/users/active-user/" + UUID.randomUUID())
                .then()
                .statusCode(401);
    }

    @Test
    void testToggleUserTypeWithoutAuth() {
        given()
                .when()
                .put("/users/user-type/" + UUID.randomUUID())
                .then()
                .statusCode(401);
    }

    @Test
    void testUploadUserImageWithoutAuth() {
        given()
                .multiPart("image", "avatar.png", new byte[]{1, 2, 3})
                .when()
                .put("/users/" + UUID.randomUUID() + "/image")
                .then()
                .statusCode(401);
    }

    @Test
    void testDeleteUserWithoutAuth(){
        given()
                .when()
                .delete("/users/" + UUID.randomUUID())
                .then()
                .statusCode(401);
    }
}