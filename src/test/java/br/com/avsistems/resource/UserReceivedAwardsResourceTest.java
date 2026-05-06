package br.com.avsistems.resource;

import br.com.avsistems.dto.request.UserReceivedAwardsRequestDto;
import br.com.avsistems.dto.response.UserReceivedAwardsResponseDto;
import br.com.avsistems.exceptions.ApplicationException;
import br.com.avsistems.exceptions.BadRequestException;
import br.com.avsistems.service.UserReceivedAwardsService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@QuarkusTest
@DisplayName("UserReceivedAwardsResource - Testes Unitários")
public class UserReceivedAwardsResourceTest {

    @InjectMock
    UserReceivedAwardsService userReceivedAwardsService;

    private UUID testId;
    private UUID testUserId;
    private UUID testAwardId;
    private UserReceivedAwardsResponseDto testResponseDto;

    @BeforeEach
    void setUp() {
        testId = UUID.randomUUID();
        testUserId = UUID.randomUUID();
        testAwardId = UUID.randomUUID();
        testResponseDto = new UserReceivedAwardsResponseDto(
                testId,
                testUserId,
                "Maria Silva",
                testAwardId,
                "Prêmio Destaque",
                "uploads/received-awards/destaque.png",
                LocalDateTime.now()
        );
    }

    @Test
    @DisplayName("POST /user-received-awards - Deve criar associação com sucesso")
    void testCreateSuccess() {
        when(userReceivedAwardsService.create(any(UserReceivedAwardsRequestDto.class)))
                .thenReturn(testResponseDto);

        given()
                .contentType("application/json")
                .body(String.format("""
                        {
                          "userId":"%s",
                          "receivedAwardId":"%s"
                        }
                        """, testUserId, testAwardId))
                .when().post("/user-received-awards")
                .then()
                .statusCode(201)
                .body("userName", equalTo("Maria Silva"))
                .body("receivedAwardName", equalTo("Prêmio Destaque"));

        verify(userReceivedAwardsService, times(1)).create(any(UserReceivedAwardsRequestDto.class));
    }

    @Test
    @DisplayName("POST /user-received-awards - Deve retornar 400 com IDs nulos")
    void testCreateWithNullIds() {
        when(userReceivedAwardsService.create(any(UserReceivedAwardsRequestDto.class)))
                .thenThrow(new BadRequestException("IDs de usuário e prêmio não podem ser nulos."));

        given()
                .contentType("application/json")
                .body("""
                        {
                          "userId":null,
                          "receivedAwardId":null
                        }
                        """)
                .when().post("/user-received-awards")
                .then()
                .statusCode(400);

        verify(userReceivedAwardsService, times(1)).create(any(UserReceivedAwardsRequestDto.class));
    }

    @Test
    @DisplayName("POST /user-received-awards - Deve retornar erro se prêmio já foi recebido")
    void testCreateWithDuplicate() {
        when(userReceivedAwardsService.create(any(UserReceivedAwardsRequestDto.class)))
                .thenThrow(new BadRequestException("O usuário já recebeu este prêmio."));

        given()
                .contentType("application/json")
                .body(String.format("""
                        {
                          "userId":"%s",
                          "receivedAwardId":"%s"
                        }
                        """, testUserId, testAwardId))
                .when().post("/user-received-awards")
                .then()
                .statusCode(400);

        verify(userReceivedAwardsService, times(1)).create(any(UserReceivedAwardsRequestDto.class));
    }

    @Test
    @DisplayName("GET /user-received-awards - Deve listar todas as associações")
    void testListAllSuccess() {
        List<UserReceivedAwardsResponseDto> list = List.of(testResponseDto);
        when(userReceivedAwardsService.listAll()).thenReturn(list);

        given()
                .when().get("/user-received-awards")
                .then()
                .statusCode(200)
                .body("size()", equalTo(1));

        verify(userReceivedAwardsService, times(1)).listAll();
    }

    @Test
    @DisplayName("GET /user-received-awards/{id} - Deve encontrar por ID")
    void testFindByIdSuccess() {
        when(userReceivedAwardsService.findById(testId))
                .thenReturn(testResponseDto);

        given()
                .when().get("/user-received-awards/" + testId)
                .then()
                .statusCode(200)
                .body("userName", equalTo("Maria Silva"));

        verify(userReceivedAwardsService, times(1)).findById(testId);
    }

    @Test
    @DisplayName("GET /user-received-awards/{id} - Deve retornar 404 quando não encontra")
    void testFindByIdNotFound() {
        UUID nonExistentId = UUID.randomUUID();
        when(userReceivedAwardsService.findById(nonExistentId))
                .thenThrow(new ApplicationException("Registro com ID: " + nonExistentId + " não encontrado."));

        given()
                .when().get("/user-received-awards/" + nonExistentId)
                .then()
                .statusCode(400);

        verify(userReceivedAwardsService, times(1)).findById(nonExistentId);
    }

    @Test
    @DisplayName("GET /user-received-awards/user/{userId} - Deve buscar por usuário")
    void testFindByUserIdSuccess() {
        List<UserReceivedAwardsResponseDto> list = List.of(testResponseDto);
        when(userReceivedAwardsService.findByUserId(testUserId))
                .thenReturn(list);

        given()
                .when().get("/user-received-awards/user/" + testUserId)
                .then()
                .statusCode(200)
                .body("size()", equalTo(1));

        verify(userReceivedAwardsService, times(1)).findByUserId(testUserId);
    }

    @Test
    @DisplayName("GET /user-received-awards/award/{receivedAwardId} - Deve buscar por prêmio")
    void testFindByAwardIdSuccess() {
        List<UserReceivedAwardsResponseDto> list = List.of(testResponseDto);
        when(userReceivedAwardsService.findByReceivedAwardId(testAwardId))
                .thenReturn(list);

        given()
                .when().get("/user-received-awards/award/" + testAwardId)
                .then()
                .statusCode(200)
                .body("size()", equalTo(1));

        verify(userReceivedAwardsService, times(1)).findByReceivedAwardId(testAwardId);
    }

    @Test
    @DisplayName("DELETE /user-received-awards/{id} - Deve deletar com sucesso")
    void testDeleteSuccess() {
        doNothing().when(userReceivedAwardsService).delete(testId);

        given()
                .when().delete("/user-received-awards/" + testId)
                .then()
                .statusCode(204);

        verify(userReceivedAwardsService, times(1)).delete(testId);
    }

    @Test
    @DisplayName("DELETE /user-received-awards/{id} - Deve retornar 404 se não existe")
    void testDeleteNotFound() {
        UUID nonExistentId = UUID.randomUUID();
        doThrow(new ApplicationException("Registro com ID: " + nonExistentId + " não encontrado."))
                .when(userReceivedAwardsService).delete(nonExistentId);

        given()
                .when().delete("/user-received-awards/" + nonExistentId)
                .then()
                .statusCode(400);

        verify(userReceivedAwardsService, times(1)).delete(nonExistentId);
    }
}
