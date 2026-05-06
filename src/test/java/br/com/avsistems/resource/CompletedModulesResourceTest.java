package br.com.avsistems.resource;

import br.com.avsistems.dto.request.CompletedModulesRequestDto;
import br.com.avsistems.dto.response.CompletedModulesResponseDto;
import br.com.avsistems.exceptions.ApplicationException;
import br.com.avsistems.exceptions.BadRequestException;
import br.com.avsistems.service.CompletedModulesService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@QuarkusTest
@DisplayName("CompletedModulesResource - Testes Unitários")
public class CompletedModulesResourceTest {

    @InjectMock
    CompletedModulesService completedModulesService;

    private UUID testModuleId;
    private CompletedModulesResponseDto testModuleDto;

    @BeforeEach
    void setUp() {
        testModuleId = UUID.randomUUID();
        testModuleDto = new CompletedModulesResponseDto(testModuleId, "Módulo de Java");
    }

    @Test
    @DisplayName("POST /completed-modules - Deve criar módulo com sucesso")
    void testCreateModuleSuccess() {
        when(completedModulesService.create(any(CompletedModulesRequestDto.class)))
                .thenReturn(testModuleDto);

        given()
                .contentType("application/json")
                .body("""
                        {
                          "name":"Módulo de Java"
                        }
                        """)
                .when().post("/completed-modules")
                .then()
                .statusCode(201)
                .body("name", equalTo("Módulo de Java"))
                .body("id", notNullValue());

        verify(completedModulesService, times(1)).create(any(CompletedModulesRequestDto.class));
    }

    @Test
    @DisplayName("POST /completed-modules - Deve retornar erro ao enviar nome vazio")
    void testCreateModuleWithEmptyName() {
        when(completedModulesService.create(any(CompletedModulesRequestDto.class)))
                .thenThrow(new BadRequestException("O nome do módulo não pode estar vazio."));

        given()
                .contentType("application/json")
                .body("""
                        {
                          "name":""
                        }
                        """)
                .when().post("/completed-modules")
                .then()
                .statusCode(400);

        verify(completedModulesService, times(1)).create(any(CompletedModulesRequestDto.class));
    }

    @Test
    @DisplayName("GET /completed-modules - Deve listar todos os módulos")
    void testListAllModules() {
        List<CompletedModulesResponseDto> modules = List.of(
                testModuleDto,
                new CompletedModulesResponseDto(UUID.randomUUID(), "Módulo de Python")
        );
        when(completedModulesService.listAll()).thenReturn(modules);

        given()
                .when().get("/completed-modules")
                .then()
                .statusCode(200)
                .body("size()", equalTo(2));

        verify(completedModulesService, times(1)).listAll();
    }

    @Test
    @DisplayName("GET /completed-modules - Deve retornar lista vazia quando não há módulos")
    void testListAllModulesEmpty() {
        when(completedModulesService.listAll()).thenReturn(List.of());

        given()
                .when().get("/completed-modules")
                .then()
                .statusCode(200)
                .body("size()", equalTo(0));

        verify(completedModulesService, times(1)).listAll();
    }

    @Test
    @DisplayName("GET /completed-modules/{id} - Deve encontrar módulo por ID")
    void testFindByIdSuccess() {
        when(completedModulesService.findById(testModuleId))
                .thenReturn(testModuleDto);

        given()
                .when().get("/completed-modules/" + testModuleId)
                .then()
                .statusCode(200)
                .body("id", equalTo(testModuleId.toString()))
                .body("name", equalTo("Módulo de Java"));

        verify(completedModulesService, times(1)).findById(testModuleId);
    }

    @Test
    @DisplayName("GET /completed-modules/{id} - Deve retornar 404 quando módulo não existe")
    void testFindByIdNotFound() {
        UUID nonExistentId = UUID.randomUUID();
        when(completedModulesService.findById(nonExistentId))
                .thenThrow(new ApplicationException("Módulo com ID: " + nonExistentId + " não encontrado."));

        given()
                .when().get("/completed-modules/" + nonExistentId)
                .then()
                .statusCode(400);

        verify(completedModulesService, times(1)).findById(nonExistentId);
    }

    @Test
    @DisplayName("GET /completed-modules/name/{name} - Deve buscar módulo por nome")
    void testFindByNameSuccess() {
        List<CompletedModulesResponseDto> modules = List.of(testModuleDto);
        when(completedModulesService.findByName("Java"))
                .thenReturn(modules);

        given()
                .when().get("/completed-modules/name/Java")
                .then()
                .statusCode(200)
                .body("size()", equalTo(1))
                .body("[0].name", equalTo("Módulo de Java"));

        verify(completedModulesService, times(1)).findByName("Java");
    }

    @Test
    @DisplayName("PUT /completed-modules/{id} - Deve atualizar módulo com sucesso")
    void testUpdateModuleSuccess() {
        CompletedModulesResponseDto updatedDto = new CompletedModulesResponseDto(testModuleId, "Módulo de Python");
        when(completedModulesService.update(eq(testModuleId), any(CompletedModulesRequestDto.class)))
                .thenReturn(updatedDto);

        given()
                .contentType("application/json")
                .body("""
                        {
                          "name":"Módulo de Python"
                        }
                        """)
                .when().put("/completed-modules/" + testModuleId)
                .then()
                .statusCode(200)
                .body("name", equalTo("Módulo de Python"));

        verify(completedModulesService, times(1)).update(eq(testModuleId), any(CompletedModulesRequestDto.class));
    }

    @Test
    @DisplayName("PUT /completed-modules/{id} - Deve retornar 404 ao atualizar módulo inexistente")
    void testUpdateModuleNotFound() {
        UUID nonExistentId = UUID.randomUUID();
        when(completedModulesService.update(eq(nonExistentId), any(CompletedModulesRequestDto.class)))
                .thenThrow(new ApplicationException("Módulo com ID: " + nonExistentId + " não encontrado."));

        given()
                .contentType("application/json")
                .body("""
                        {
                          "name":"Novo Nome"
                        }
                        """)
                .when().put("/completed-modules/" + nonExistentId)
                .then()
                .statusCode(400);

        verify(completedModulesService, times(1)).update(eq(nonExistentId), any(CompletedModulesRequestDto.class));
    }

    @Test
    @DisplayName("DELETE /completed-modules/{id} - Deve deletar módulo com sucesso")
    void testDeleteModuleSuccess() {
        doNothing().when(completedModulesService).delete(testModuleId);

        given()
                .when().delete("/completed-modules/" + testModuleId)
                .then()
                .statusCode(204);

        verify(completedModulesService, times(1)).delete(testModuleId);
    }

    @Test
    @DisplayName("DELETE /completed-modules/{id} - Deve retornar 404 ao deletar módulo inexistente")
    void testDeleteModuleNotFound() {
        UUID nonExistentId = UUID.randomUUID();
        doThrow(new ApplicationException("Módulo com ID: " + nonExistentId + " não encontrado."))
                .when(completedModulesService).delete(nonExistentId);

        given()
                .when().delete("/completed-modules/" + nonExistentId)
                .then()
                .statusCode(400);

        verify(completedModulesService, times(1)).delete(nonExistentId);
    }
}
