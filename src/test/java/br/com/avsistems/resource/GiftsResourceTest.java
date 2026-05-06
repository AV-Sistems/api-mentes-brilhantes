package br.com.avsistems.resource;

import br.com.avsistems.dto.request.GiftsRequestDto;
import br.com.avsistems.dto.response.GiftsResponseDto;
import br.com.avsistems.exceptions.NotFoundException;
import br.com.avsistems.service.GiftsService;
import br.com.avsistems.type.GiftsStatus;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@QuarkusTest
@DisplayName("GiftsResource - Testes Unitários")
class GiftsResourceTest {

    @InjectMock
    GiftsService giftsService;

    private UUID giftsId;
    private GiftsResponseDto giftsDto;

    @BeforeEach
    void setUp() {
        giftsId = UUID.randomUUID();
        giftsDto = new GiftsResponseDto(
                giftsId,
                "Fone Bluetooth",
                300,
                false,
                12,
                GiftsStatus.ACTIVE,
                UUID.randomUUID(),
                "Parceiro Teste"
        );
    }

    @Test
    void testListAllGifts() {
        when(giftsService.listAll()).thenReturn(List.of(giftsDto));

        given()
                .when().get("/gifts")
                .then()
                .statusCode(200)
                .body("$", hasSize(1))
                .body("[0].name", equalTo("Fone Bluetooth"));

        verify(giftsService, times(1)).listAll();
    }

    @Test
    void testListAvailableGifts() {
        when(giftsService.listAvailable()).thenReturn(List.of(giftsDto));

        given()
                .when().get("/gifts/available")
                .then()
                .statusCode(200)
                .body("$", hasSize(1));

        verify(giftsService, times(1)).listAvailable();
    }

    @Test
    void testFindGiftsByIdSuccess() {
        when(giftsService.findById(giftsId)).thenReturn(giftsDto);

        given()
                .when().get("/gifts/" + giftsId)
                .then()
                .statusCode(200)
                .body("id", equalTo(giftsId.toString()))
                .body("status", equalTo("ACTIVE"));

        verify(giftsService, times(1)).findById(giftsId);
    }

    @Test
    void testFindGiftsByIdNotFound() {
        when(giftsService.findById(giftsId)).thenThrow(new NotFoundException("Brinde não encontrado."));

        given()
                .when().get("/gifts/" + giftsId)
                .then()
                .statusCode(404)
                .body("message", containsString("não encontrado"));
    }

    @Test
    void testCreateGiftSuccess() {
        when(giftsService.createGift(any(GiftsRequestDto.class))).thenReturn(giftsDto);

        given()
                .contentType("application/json")
                .body("""
                        {
                          "name":"Fone Bluetooth",
                          "pointsCost":300,
                          "isAdvanced":false,
                          "stock":12,
                          "status":"ACTIVE"
                        }
                        """)
                .when().post("/gifts")
                .then()
                .statusCode(201)
                .body("name", equalTo("Fone Bluetooth"));

        verify(giftsService, times(1)).createGift(any(GiftsRequestDto.class));
    }

    @Test
    void testUpdateGiftSuccess() {
        when(giftsService.updateGift(eq(giftsId), any(GiftsRequestDto.class))).thenReturn(giftsDto);

        given()
                .contentType("application/json")
                .body("{" +
                        "\"name\":\"Fone Bluetooth\"," +
                        "\"pointsCost\":300," +
                        "\"isAdvanced\":false," +
                        "\"stock\":12," +
                        "\"status\":\"ACTIVE\"}")
                .when().put("/gifts/" + giftsId)
                .then()
                .statusCode(200)
                .body("id", equalTo(giftsId.toString()));

        verify(giftsService, times(1)).updateGift(eq(giftsId), any(GiftsRequestDto.class));
    }

    @Test
    void testToggleStatusSuccess() {
        GiftsResponseDto inactive = new GiftsResponseDto(
                giftsDto.id(),
                giftsDto.name(),
                giftsDto.pointsCost(),
                giftsDto.isAdvanced(),
                giftsDto.stock(),
                GiftsStatus.INACTIVE,
                giftsDto.partnerId(),
                giftsDto.partnerName()
        );
        when(giftsService.toggleStatus(giftsId)).thenReturn(inactive);

        given()
                .when().patch("/gifts/" + giftsId + "/status")
                .then()
                .statusCode(200)
                .body("status", equalTo("INACTIVE"));

        verify(giftsService, times(1)).toggleStatus(giftsId);
    }

    @Test
    void testDeleteGiftSuccess() {
        doNothing().when(giftsService).deleteGift(giftsId);

        given()
                .when().delete("/gifts/" + giftsId)
                .then()
                .statusCode(204);

        verify(giftsService, times(1)).deleteGift(giftsId);
    }

    @Test
    void testDeleteGiftNotFound() {
        doThrow(new NotFoundException("Brinde não encontrado.")).when(giftsService).deleteGift(giftsId);

        given()
                .when().delete("/gifts/" + giftsId)
                .then()
                .statusCode(404)
                .body("message", containsString("não encontrado"));
    }

    @Test
    void testListAllGiftsEmpty() {
        when(giftsService.listAll()).thenReturn(List.of());

        given()
                .when().get("/gifts")
                .then()
                .statusCode(200)
                .body("$", empty());
    }
}


