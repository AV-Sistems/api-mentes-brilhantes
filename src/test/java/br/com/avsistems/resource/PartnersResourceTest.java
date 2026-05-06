package br.com.avsistems.resource;

import br.com.avsistems.dto.request.PartnerMultipartForm;
import br.com.avsistems.dto.response.PartnersResponseDto;
import br.com.avsistems.exceptions.BadRequestException;
import br.com.avsistems.exceptions.NotFoundException;
import br.com.avsistems.service.PartnersService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@QuarkusTest
@DisplayName("PartnersResource - Testes Unitários")
class PartnersResourceTest {

    @InjectMock
    PartnersService partnersService;

    private UUID testPartnerId;
    private PartnersResponseDto testPartnerDto;

    @BeforeEach
    void setUp() {
        testPartnerId = UUID.randomUUID();
        testPartnerDto = new PartnersResponseDto(
                testPartnerId,
                "Amazon",
                "uploads/partners/teste.jpg",
                "https://amazon.com",
                LocalDate.of(2026, 12, 31),
                "São Paulo",
                "SP",
                "01310-100"
        );
    }

    // ==================== POST /partners ====================

    @Test
    @DisplayName("POST /partners - Deve criar parceiro com sucesso")
    void testCreatePartnerSuccess() {
        when(partnersService.createPartner(any(PartnerMultipartForm.class)))
                .thenReturn(testPartnerDto);

        given()
                .multiPart("data", "{\"name\":\"Amazon\",\"url\":\"https://amazon.com\",\"validity\":\"2026-12-31\",\"city\":\"São Paulo\",\"state\":\"SP\",\"zipCode\":\"01310-100\"}")
                .multiPart("image", "test.jpg", new byte[]{1, 2, 3})
                .when()
                .post("/partners")
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("name", equalTo("Amazon"))
                .body("url", equalTo("https://amazon.com"))
                .body("state", equalTo("SP"));

        verify(partnersService, times(1)).createPartner(any(PartnerMultipartForm.class));
    }

    @Test
    @DisplayName("POST /partners - Deve retornar 400 quando JSON é inválido")
    void testCreatePartnerInvalidJson() {
        when(partnersService.createPartner(any(PartnerMultipartForm.class)))
                .thenThrow(new BadRequestException("Dados inválidos no campo 'data'"));

        given()
                .multiPart("data", "{invalid json}")
                .when()
                .post("/partners")
                .then()
                .statusCode(400)
                .body("message", containsString("Dados inválidos"));
    }

    @Test
    @DisplayName("POST /partners - Deve retornar 400 quando campo 'data' está vazio")
    void testCreatePartnerEmptyData() {
        when(partnersService.createPartner(any(PartnerMultipartForm.class)))
                .thenThrow(new BadRequestException("O campo 'data' com os dados do parceiro é obrigatório"));

        given()
                .multiPart("data", "")
                .when()
                .post("/partners")
                .then()
                .statusCode(400)
                .body("message", containsString("obrigatório"));
    }

    @Test
    @DisplayName("POST /partners - Deve retornar 400 quando nome está vazio")
    void testCreatePartnerEmptyName() {
        when(partnersService.createPartner(any(PartnerMultipartForm.class)))
                .thenThrow(new BadRequestException("O nome do parceiro não pode ser vazio"));

        given()
                .multiPart("data", "{\"name\":\"\",\"url\":\"https://amazon.com\"}")
                .when()
                .post("/partners")
                .then()
                .statusCode(400)
                .body("message", containsString("nome"));
    }

    // ==================== PUT /partners/{id} ====================

    @Test
    @DisplayName("PUT /partners/{id} - Deve atualizar parceiro com sucesso")
    void testUpdatePartnerSuccess() {
        PartnersResponseDto updatedDto = new PartnersResponseDto(
                testPartnerId,
                "Amazon Atualizado",
                testPartnerDto.imageUrl(),
                "https://amazon-updated.com",
                testPartnerDto.validity(),
                testPartnerDto.city(),
                testPartnerDto.state(),
                testPartnerDto.zipCode()
        );

        when(partnersService.updatePartner(eq(testPartnerId), any(PartnerMultipartForm.class)))
                .thenReturn(updatedDto);

        given()
                .multiPart("data", "{\"name\":\"Amazon Atualizado\",\"url\":\"https://amazon-updated.com\"}")
                .when()
                .put("/partners/" + testPartnerId)
                .then()
                .statusCode(200)
                .body("name", equalTo("Amazon Atualizado"));

        verify(partnersService, times(1)).updatePartner(eq(testPartnerId), any(PartnerMultipartForm.class));
    }

    @Test
    @DisplayName("PUT /partners/{id} - Deve retornar 404 quando parceiro não existe")
    void testUpdatePartnerNotFound() {
        when(partnersService.updatePartner(eq(testPartnerId), any(PartnerMultipartForm.class)))
                .thenThrow(new NotFoundException("Nenhum parceiro encontrado com o id: " + testPartnerId));

        given()
                .multiPart("data", "{\"name\":\"Amazon\"}")
                .when()
                .put("/partners/" + testPartnerId)
                .then()
                .statusCode(404)
                .body("message", containsString("Nenhum parceiro encontrado"));
    }

    @Test
    @DisplayName("PUT /partners/{id} - Deve retornar 400 quando form é nulo")
    void testUpdatePartnerNullForm() {
        when(partnersService.updatePartner(eq(testPartnerId), any(PartnerMultipartForm.class)))
                .thenThrow(new BadRequestException("Formulário inválido"));

        given()
                .when()
                .put("/partners/" + testPartnerId)
                .then()
                .statusCode(400);
    }

    // ==================== DELETE /partners/{id} ====================

    @Test
    @DisplayName("DELETE /partners/{id} - Deve deletar parceiro com sucesso")
    void testDeletePartnerSuccess() {
        doNothing().when(partnersService).deletePartner(testPartnerId);

        given()
                .when()
                .delete("/partners/" + testPartnerId)
                .then()
                .statusCode(204);

        verify(partnersService, times(1)).deletePartner(testPartnerId);
    }

    @Test
    @DisplayName("DELETE /partners/{id} - Deve retornar 404 quando parceiro não existe")
    void testDeletePartnerNotFound() {
        doThrow(new NotFoundException("Nenhum parceiro encontrado com o id: " + testPartnerId))
                .when(partnersService).deletePartner(testPartnerId);

        given()
                .when()
                .delete("/partners/" + testPartnerId)
                .then()
                .statusCode(404)
                .body("message", containsString("Nenhum parceiro encontrado"));
    }

    // ==================== GET /partners ====================

    @Test
    @DisplayName("GET /partners - Deve retornar lista de todos os parceiros")
    void testFindAllSuccess() {
        List<PartnersResponseDto> partners = List.of(testPartnerDto);
        when(partnersService.findAll()).thenReturn(partners);

        given()
                .when()
                .get("/partners")
                .then()
                .statusCode(200)
                .body("$", hasSize(1))
                .body("[0].name", equalTo("Amazon"))
                .body("[0].state", equalTo("SP"));

        verify(partnersService, times(1)).findAll();
    }

    @Test
    @DisplayName("GET /partners - Deve retornar lista vazia quando não há parceiros")
    void testFindAllEmpty() {
        when(partnersService.findAll()).thenReturn(List.of());

        given()
                .when()
                .get("/partners")
                .then()
                .statusCode(200)
                .body("$", empty());
    }

    // ==================== GET /partners/validity ====================

    @Test
    @DisplayName("GET /partners/validity - Deve retornar parceiros válidos por localização")
    void testFindByValidityAndStateAndCitySuccess() {
        List<PartnersResponseDto> partners = List.of(testPartnerDto);
        when(partnersService.FindByValidityAndStateAndCity("SP", "São Paulo"))
                .thenReturn(partners);

        given()
                .when()
                .get("/partners/validity/SP/São Paulo")
                .then()
                .statusCode(200)
                .body("$", hasSize(1))
                .body("[0].city", equalTo("São Paulo"));

        verify(partnersService, times(1)).FindByValidityAndStateAndCity("SP", "São Paulo");
    }

    @Test
    @DisplayName("GET /partners/validity - Deve retornar lista vazia quando não há parceiros válidos")
    void testFindByValidityAndStateAndCityEmpty() {
        when(partnersService.FindByValidityAndStateAndCity("RJ", "Rio de Janeiro"))
                .thenReturn(List.of());

        given()
                .when()
                .get("/partners/validity/RJ/Rio de Janeiro")
                .then()
                .statusCode(200)
                .body("$", empty());
    }

    // ==================== GET /partners/validity (all) ====================

    @Test
    @DisplayName("GET /partners/validity (all) - Deve retornar todos os parceiros válidos")
    void testFindAllByValiditySuccess() {
        List<PartnersResponseDto> partners = List.of(testPartnerDto);
        when(partnersService.findAllByValidity()).thenReturn(partners);

        given()
                .when()
                .get("/partners/validity")
                .then()
                .statusCode(200)
                .body("$", hasSize(1));

        verify(partnersService, times(1)).findAllByValidity();
    }


}

