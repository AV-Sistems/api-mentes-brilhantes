package br.com.avsistems.resource;

import br.com.avsistems.dto.response.ReceivedAwardsResponseDto;
import br.com.avsistems.exceptions.ApplicationException;
import br.com.avsistems.exceptions.BadRequestException;
import br.com.avsistems.service.ReceivedAwardsService;
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
@DisplayName("ReceivedAwardsResource - Testes Unitários")
public class ReceivedAwardsResourceTest {

    @InjectMock
    ReceivedAwardsService receivedAwardsService;

    private UUID testAwardId;
    private ReceivedAwardsResponseDto testAwardDto;

    @BeforeEach
    void setUp() {
        testAwardId = UUID.randomUUID();
        testAwardDto = new ReceivedAwardsResponseDto(testAwardId, "Prêmio Destaque", "uploads/received-awards/destaque.png");
    }

    @Test
    @DisplayName("POST /received-awards - Deve criar prêmio com sucesso")
    void testCreateAwardSuccess() {
        when(receivedAwardsService.create(any()))
                .thenReturn(testAwardDto);

        given()
                .multiPart("data", """
                        {"name":"Prêmio Destaque"}
                        """, "text/plain")
                .when().post("/received-awards")
                .then()
                .statusCode(201)
                .body("name", equalTo("Prêmio Destaque"))
                .body("id", notNullValue())
                .body("imageUrl", equalTo("uploads/received-awards/destaque.png"));

        verify(receivedAwardsService, times(1)).create(any());
    }

    @Test
    @DisplayName("POST /received-awards - Deve retornar erro ao enviar nome vazio")
    void testCreateAwardWithEmptyName() {
        when(receivedAwardsService.create(any()))
                .thenThrow(new BadRequestException("O nome do prêmio não pode estar vazio."));

        given()
                .multiPart("data", """
                        {"name":""}
                        """, "text/plain")
                .when().post("/received-awards")
                .then()
                .statusCode(400);

        verify(receivedAwardsService, times(1)).create(any());
    }

    @Test
    @DisplayName("GET /received-awards - Deve listar todos os prêmios")
    void testListAllAwards() {
        List<ReceivedAwardsResponseDto> awards = List.of(
                testAwardDto,
                new ReceivedAwardsResponseDto(UUID.randomUUID(), "Prêmio Melhor Desempenho", null)
        );
        when(receivedAwardsService.listAll()).thenReturn(awards);

        given()
                .when().get("/received-awards")
                .then()
                .statusCode(200)
                .body("size()", equalTo(2));

        verify(receivedAwardsService, times(1)).listAll();
    }

    @Test
    @DisplayName("GET /received-awards - Deve retornar lista vazia quando não há prêmios")
    void testListAllAwardsEmpty() {
        when(receivedAwardsService.listAll()).thenReturn(List.of());

        given()
                .when().get("/received-awards")
                .then()
                .statusCode(200)
                .body("size()", equalTo(0));

        verify(receivedAwardsService, times(1)).listAll();
    }

    @Test
    @DisplayName("GET /received-awards/{id} - Deve encontrar prêmio por ID")
    void testFindByIdSuccess() {
        when(receivedAwardsService.findById(testAwardId))
                .thenReturn(testAwardDto);

        given()
                .when().get("/received-awards/" + testAwardId)
                .then()
                .statusCode(200)
                .body("id", equalTo(testAwardId.toString()))
                .body("name", equalTo("Prêmio Destaque"));

        verify(receivedAwardsService, times(1)).findById(testAwardId);
    }

    @Test
    @DisplayName("GET /received-awards/{id} - Deve retornar 404 quando prêmio não existe")
    void testFindByIdNotFound() {
        UUID nonExistentId = UUID.randomUUID();
        when(receivedAwardsService.findById(nonExistentId))
                .thenThrow(new ApplicationException("Prêmio com ID: " + nonExistentId + " não encontrado."));

        given()
                .when().get("/received-awards/" + nonExistentId)
                .then()
                .statusCode(400);

        verify(receivedAwardsService, times(1)).findById(nonExistentId);
    }

    @Test
    @DisplayName("GET /received-awards/name/{name} - Deve buscar prêmio por nome")
    void testFindByNameSuccess() {
        List<ReceivedAwardsResponseDto> awards = List.of(testAwardDto);
        when(receivedAwardsService.findByName("Destaque"))
                .thenReturn(awards);

        given()
                .when().get("/received-awards/name/Destaque")
                .then()
                .statusCode(200)
                .body("size()", equalTo(1))
                .body("[0].name", equalTo("Prêmio Destaque"));

        verify(receivedAwardsService, times(1)).findByName("Destaque");
    }

    @Test
    @DisplayName("PUT /received-awards/{id} - Deve atualizar prêmio com sucesso")
    void testUpdateAwardSuccess() {
        ReceivedAwardsResponseDto updatedDto = new ReceivedAwardsResponseDto(testAwardId, "Prêmio Top Destaque", "uploads/received-awards/top.png");
        when(receivedAwardsService.update(eq(testAwardId), any()))
                .thenReturn(updatedDto);

        given()
                .multiPart("data", """
                        {"name":"Prêmio Top Destaque"}
                        """, "text/plain")
                .when().put("/received-awards/" + testAwardId)
                .then()
                .statusCode(200)
                .body("name", equalTo("Prêmio Top Destaque"))
                .body("imageUrl", equalTo("uploads/received-awards/top.png"));

        verify(receivedAwardsService, times(1)).update(eq(testAwardId), any());
    }

    @Test
    @DisplayName("PUT /received-awards/{id} - Deve retornar 404 ao atualizar prêmio inexistente")
    void testUpdateAwardNotFound() {
        UUID nonExistentId = UUID.randomUUID();
        when(receivedAwardsService.update(eq(nonExistentId), any()))
                .thenThrow(new ApplicationException("Prêmio com ID: " + nonExistentId + " não encontrado."));

        given()
                .multiPart("data", """
                        {"name":"Novo Nome"}
                        """, "text/plain")
                .when().put("/received-awards/" + nonExistentId)
                .then()
                .statusCode(400);

        verify(receivedAwardsService, times(1)).update(eq(nonExistentId), any());
    }

    @Test
    @DisplayName("DELETE /received-awards/{id} - Deve deletar prêmio com sucesso")
    void testDeleteAwardSuccess() {
        doNothing().when(receivedAwardsService).delete(testAwardId);

        given()
                .when().delete("/received-awards/" + testAwardId)
                .then()
                .statusCode(204);

        verify(receivedAwardsService, times(1)).delete(testAwardId);
    }

    @Test
    @DisplayName("DELETE /received-awards/{id} - Deve retornar 404 ao deletar prêmio inexistente")
    void testDeleteAwardNotFound() {
        UUID nonExistentId = UUID.randomUUID();
        doThrow(new ApplicationException("Prêmio com ID: " + nonExistentId + " não encontrado."))
                .when(receivedAwardsService).delete(nonExistentId);

        given()
                .when().delete("/received-awards/" + nonExistentId)
                .then()
                .statusCode(400);

        verify(receivedAwardsService, times(1)).delete(nonExistentId);
    }
}
