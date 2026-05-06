package br.com.avsistems.resource;

import br.com.avsistems.dto.response.MentesEditionResponseDto;
import br.com.avsistems.service.MentesEditionService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@QuarkusTest
class MentesEditionResourceTest {

    @InjectMock
    MentesEditionService mentesEditionService;

    private MentesEditionResponseDto dto;

    @BeforeEach
    void setUp() {
        dto = new MentesEditionResponseDto(
                UUID.randomUUID(),
                "Edicao 2026",
                LocalDate.of(2026, 8, 20),
                "88000-000",
                "Florianopolis",
                "SC"
        );
    }

    @Test
    void testListAll() {
        when(mentesEditionService.listAllMentesEdition()).thenReturn(List.of(dto));

        given()
                .when()
                .get("/mentes")
                .then()
                .statusCode(200)
                .body("$", hasSize(1))
                .body("[0].title", equalTo("Edicao 2026"));

        verify(mentesEditionService, times(1)).listAllMentesEdition();
    }

    @Test
    void testFindByStateCityAndTitleRoutes() {
        when(mentesEditionService.findByStateAndCity(any(), any())).thenReturn(List.of(dto));
        when(mentesEditionService.findByName(any())).thenReturn(dto);

        given().when().get("/mentes/SC/Florianopolis").then().statusCode(200).body("$", hasSize(1));
        given().when().get("/mentes/title/Edicao 2026").then().statusCode(200).body("title", equalTo("Edicao 2026"));

        verify(mentesEditionService, times(1)).findByStateAndCity(any(), any());
        verify(mentesEditionService, times(1)).findByName(any());
    }

    @Test
    void testSecuredEndpointsWithoutAuth() {
        given()
                .contentType("application/json")
                .body("{\"title\":\"Edicao 2026\",\"dateEdition\":\"2026-08-20\",\"zipCode\":\"88000-000\",\"city\":\"Florianopolis\",\"state\":\"SC\"}")
                .when()
                .post("/mentes")
                .then()
                .statusCode(401);

        given()
                .contentType("application/json")
                .body("{\"title\":\"Edicao 2027\"}")
                .when()
                .put("/mentes/" + UUID.randomUUID())
                .then()
                .statusCode(401);

        given()
                .when()
                .delete("/mentes/" + UUID.randomUUID())
                .then()
                .statusCode(401);
    }
}

