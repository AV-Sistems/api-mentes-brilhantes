package br.com.avsistems.resource;

import br.com.avsistems.dto.request.UserCompletedModulesRequestDto;
import br.com.avsistems.dto.response.UserCompletedModulesResponseDto;
import br.com.avsistems.exceptions.ApplicationException;
import br.com.avsistems.exceptions.BadRequestException;
import br.com.avsistems.service.UserCompletedModulesService;
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
@DisplayName("UserCompletedModulesResource - Testes Unitários")
public class UserCompletedModulesResourceTest {

    @InjectMock
    UserCompletedModulesService userCompletedModulesService;

    private UUID testId;
    private UUID testUserId;
    private UUID testModuleId;
    private UserCompletedModulesResponseDto testResponseDto;

    @BeforeEach
    void setUp() {
        testId = UUID.randomUUID();
        testUserId = UUID.randomUUID();
        testModuleId = UUID.randomUUID();
        testResponseDto = new UserCompletedModulesResponseDto(
                testId,
                testUserId,
                "João Silva",
                testModuleId,
                "Módulo Java",
                LocalDateTime.now()
        );
    }

    @Test
    @DisplayName("POST /user-completed-modules - Deve criar associação com sucesso")
    void testCreateSuccess() {
        when(userCompletedModulesService.create(any(UserCompletedModulesRequestDto.class)))
                .thenReturn(testResponseDto);

        given()
                .contentType("application/json")
                .body(String.format("""
                        {
                          "userId":"%s",
                          "completedModuleId":"%s"
                        }
                        """, testUserId, testModuleId))
                .when().post("/user-completed-modules")
                .then()
                .statusCode(201)
                .body("userName", equalTo("João Silva"))
                .body("completedModuleName", equalTo("Módulo Java"));

        verify(userCompletedModulesService, times(1)).create(any(UserCompletedModulesRequestDto.class));
    }

    @Test
    @DisplayName("POST /user-completed-modules - Deve retornar 400 com IDs nulos")
    void testCreateWithNullIds() {
        when(userCompletedModulesService.create(any(UserCompletedModulesRequestDto.class)))
                .thenThrow(new BadRequestException("IDs de usuário e módulo não podem ser nulos."));

        given()
                .contentType("application/json")
                .body("""
                        {
                          "userId":null,
                          "completedModuleId":null
                        }
                        """)
                .when().post("/user-completed-modules")
                .then()
                .statusCode(400);

        verify(userCompletedModulesService, times(1)).create(any(UserCompletedModulesRequestDto.class));
    }

    @Test
    @DisplayName("POST /user-completed-modules - Deve retornar erro se módulo já foi completado")
    void testCreateWithDuplicate() {
        when(userCompletedModulesService.create(any(UserCompletedModulesRequestDto.class)))
                .thenThrow(new BadRequestException("O usuário já completou este módulo."));

        given()
                .contentType("application/json")
                .body(String.format("""
                        {
                          "userId":"%s",
                          "completedModuleId":"%s"
                        }
                        """, testUserId, testModuleId))
                .when().post("/user-completed-modules")
                .then()
                .statusCode(400);

        verify(userCompletedModulesService, times(1)).create(any(UserCompletedModulesRequestDto.class));
    }

    @Test
    @DisplayName("GET /user-completed-modules - Deve listar todas as associações")
    void testListAllSuccess() {
        List<UserCompletedModulesResponseDto> list = List.of(testResponseDto);
        when(userCompletedModulesService.listAll()).thenReturn(list);

        given()
                .when().get("/user-completed-modules")
                .then()
                .statusCode(200)
                .body("size()", equalTo(1));

        verify(userCompletedModulesService, times(1)).listAll();
    }

    @Test
    @DisplayName("GET /user-completed-modules/{id} - Deve encontrar por ID")
    void testFindByIdSuccess() {
        when(userCompletedModulesService.findById(testId))
                .thenReturn(testResponseDto);

        given()
                .when().get("/user-completed-modules/" + testId)
                .then()
                .statusCode(200)
                .body("userName", equalTo("João Silva"));

        verify(userCompletedModulesService, times(1)).findById(testId);
    }

    @Test
    @DisplayName("GET /user-completed-modules/{id} - Deve retornar 404 quando não encontra")
    void testFindByIdNotFound() {
        UUID nonExistentId = UUID.randomUUID();
        when(userCompletedModulesService.findById(nonExistentId))
                .thenThrow(new ApplicationException("Registro com ID: " + nonExistentId + " não encontrado."));

        given()
                .when().get("/user-completed-modules/" + nonExistentId)
                .then()
                .statusCode(400);

        verify(userCompletedModulesService, times(1)).findById(nonExistentId);
    }

    @Test
    @DisplayName("GET /user-completed-modules/user/{userId} - Deve buscar por usuário")
    void testFindByUserIdSuccess() {
        List<UserCompletedModulesResponseDto> list = List.of(testResponseDto);
        when(userCompletedModulesService.findByUserId(testUserId))
                .thenReturn(list);

        given()
                .when().get("/user-completed-modules/user/" + testUserId)
                .then()
                .statusCode(200)
                .body("size()", equalTo(1));

        verify(userCompletedModulesService, times(1)).findByUserId(testUserId);
    }

    @Test
    @DisplayName("GET /user-completed-modules/module/{completedModuleId} - Deve buscar por módulo")
    void testFindByModuleIdSuccess() {
        List<UserCompletedModulesResponseDto> list = List.of(testResponseDto);
        when(userCompletedModulesService.findByCompletedModuleId(testModuleId))
                .thenReturn(list);

        given()
                .when().get("/user-completed-modules/module/" + testModuleId)
                .then()
                .statusCode(200)
                .body("size()", equalTo(1));

        verify(userCompletedModulesService, times(1)).findByCompletedModuleId(testModuleId);
    }

    @Test
    @DisplayName("DELETE /user-completed-modules/{id} - Deve deletar com sucesso")
    void testDeleteSuccess() {
        doNothing().when(userCompletedModulesService).delete(testId);

        given()
                .when().delete("/user-completed-modules/" + testId)
                .then()
                .statusCode(204);

        verify(userCompletedModulesService, times(1)).delete(testId);
    }

    @Test
    @DisplayName("DELETE /user-completed-modules/{id} - Deve retornar 404 se não existe")
    void testDeleteNotFound() {
        UUID nonExistentId = UUID.randomUUID();
        doThrow(new ApplicationException("Registro com ID: " + nonExistentId + " não encontrado."))
                .when(userCompletedModulesService).delete(nonExistentId);

        given()
                .when().delete("/user-completed-modules/" + nonExistentId)
                .then()
                .statusCode(400);

        verify(userCompletedModulesService, times(1)).delete(nonExistentId);
    }
}
