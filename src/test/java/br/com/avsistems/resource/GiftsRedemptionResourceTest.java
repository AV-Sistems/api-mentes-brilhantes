package br.com.avsistems.resource;

import br.com.avsistems.dto.request.GiftsRedemptionRequestDto;
import br.com.avsistems.dto.response.GiftsResponseDto;
import br.com.avsistems.dto.response.GiftsRedemptionResponseDto;
import br.com.avsistems.dto.response.UserResponseDto;
import br.com.avsistems.service.GiftsRedemptionService;
import br.com.avsistems.type.GiftsRedemptionStatus;
import br.com.avsistems.type.GiftsStatus;
import br.com.avsistems.type.UserType;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@QuarkusTest
class GiftsRedemptionResourceTest {

    @InjectMock
    GiftsRedemptionService giftsRedemptionService;

    private UUID redemptionId;
    private UUID userId;
    private GiftsRedemptionResponseDto redemptionDto;

    @BeforeEach
    void setUp() {
        redemptionId = UUID.randomUUID();
        userId = UUID.randomUUID();

        GiftsResponseDto gifts = new GiftsResponseDto(
                UUID.randomUUID(),
                "Premio X",
                100,
                false,
                10,
                GiftsStatus.ACTIVE,
                UUID.randomUUID(),
                "Parceiro"
        );

        UserResponseDto user = new UserResponseDto(
                userId,
                "Usuario",
                "user@teste.com",
                UserType.USER,
                200,
                200,
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

        redemptionDto = new GiftsRedemptionResponseDto(
                redemptionId,
                gifts,
                user,
                GiftsRedemptionStatus.PENDING,
                100,
                java.time.LocalDateTime.now(),
                java.time.LocalDateTime.now()
        );
    }

    @Test
    void testListAll() {
        when(giftsRedemptionService.listAll()).thenReturn(List.of(redemptionDto));

        given()
                .when()
                .get("/gifts-redemptions")
                .then()
                .statusCode(200)
                .body("$", hasSize(1));

        verify(giftsRedemptionService, times(1)).listAll();
    }

    @Test
    void testListPending() {
        when(giftsRedemptionService.listPending()).thenReturn(List.of(redemptionDto));

        given()
                .when()
                .get("/gifts-redemptions/pending")
                .then()
                .statusCode(200)
                .body("$", hasSize(1));

        verify(giftsRedemptionService, times(1)).listPending();
    }

    @Test
    void testListByUser() {
        when(giftsRedemptionService.listByUser(userId)).thenReturn(List.of(redemptionDto));

        given()
                .when()
                .get("/gifts-redemptions/user/" + userId)
                .then()
                .statusCode(200)
                .body("$", hasSize(1));

        verify(giftsRedemptionService, times(1)).listByUser(userId);
    }

    @Test
    void testCreateRedemption() {
        when(giftsRedemptionService.createRedemption(any(GiftsRedemptionRequestDto.class))).thenReturn(redemptionDto);

        given()
                .contentType("application/json")
                .body("{\"userId\":\"" + userId + "\",\"giftsId\":\"" + redemptionDto.gifts().id() + "\"}")
                .when()
                .post("/gifts-redemptions")
                .then()
                .statusCode(201)
                .body("id", equalTo(redemptionId.toString()));

        verify(giftsRedemptionService, times(1)).createRedemption(any(GiftsRedemptionRequestDto.class));
    }

    @Test
    void testValidateAndCancel() {
        GiftsRedemptionResponseDto validated = new GiftsRedemptionResponseDto(
                redemptionDto.id(), redemptionDto.gifts(), redemptionDto.user(), GiftsRedemptionStatus.VALIDATED,
                redemptionDto.pointsUsed(), redemptionDto.createdAt(), redemptionDto.updatedAt());
        GiftsRedemptionResponseDto cancelled = new GiftsRedemptionResponseDto(
                redemptionDto.id(), redemptionDto.gifts(), redemptionDto.user(), GiftsRedemptionStatus.CANCELLED,
                redemptionDto.pointsUsed(), redemptionDto.createdAt(), redemptionDto.updatedAt());

        when(giftsRedemptionService.validateRedemption(redemptionId)).thenReturn(validated);
        when(giftsRedemptionService.cancelRedemption(redemptionId)).thenReturn(cancelled);

        given().when().put("/gifts-redemptions/" + redemptionId + "/validate")
                .then().statusCode(200).body("status", equalTo("VALIDATED"));
        given().when().put("/gifts-redemptions/" + redemptionId + "/cancel")
                .then().statusCode(200).body("status", equalTo("CANCELLED"));

        verify(giftsRedemptionService, times(1)).validateRedemption(redemptionId);
        verify(giftsRedemptionService, times(1)).cancelRedemption(redemptionId);
    }
}


