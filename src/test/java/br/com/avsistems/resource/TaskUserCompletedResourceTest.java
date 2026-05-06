package br.com.avsistems.resource;

import br.com.avsistems.dto.request.TaskUserCompletedRequestDto;
import br.com.avsistems.dto.response.TaskResponseDto;
import br.com.avsistems.dto.response.TaskUserCompletedResponseDto;
import br.com.avsistems.dto.response.UserResponseDto;
import br.com.avsistems.service.TaskUserCompletedService;
import br.com.avsistems.type.TasksStatus;
import br.com.avsistems.type.TasksType;
import br.com.avsistems.type.UserType;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@QuarkusTest
@DisplayName("TaskUserCompletedResource - Testes Unitarios")
class TaskUserCompletedResourceTest {

    @InjectMock
    TaskUserCompletedService taskUserCompletedService;

    private UUID completedId;
    private UUID taskId;
    private UUID userId;
    private TaskUserCompletedResponseDto completedDto;

    @BeforeEach
    void setUp() {
        completedId = UUID.randomUUID();
        taskId = UUID.randomUUID();
        userId = UUID.randomUUID();

        TaskResponseDto task = new TaskResponseDto(
                taskId,
                "Desafio 1",
                20,
                TasksStatus.ACTIVE,
                null,
                TasksType.NORMAL,
                30
        );

        UserResponseDto user = new UserResponseDto(
                userId,
                "Usuario Teste",
                "usuario@teste.com",
                UserType.USER,
                100,
                100,
                "/uploads/users/user.png",
                "UFSC",
                null,
                null,
                null,
                null,
                "Rua A",
                "10",
                "Centro",
                "Florianopolis",
                "SC",
                "Apto 1",
                "88000-000"
        );

        completedDto = new TaskUserCompletedResponseDto(
                completedId,
                task,
                user,
                false,
                LocalDateTime.now()
        );
    }

    @Test
    void testListAll() {
        when(taskUserCompletedService.listAll()).thenReturn(List.of(completedDto));

        given()
                .when()
                .get("/completed-task")
                .then()
                .statusCode(200)
                .body("$", hasSize(1));

        verify(taskUserCompletedService, times(1)).listAll();
    }

    @Test
    void testFindById() {
        when(taskUserCompletedService.findById(completedId)).thenReturn(completedDto);

        given()
                .when()
                .get("/completed-task/" + completedId)
                .then()
                .statusCode(200)
                .body("id", equalTo(completedId.toString()));

        verify(taskUserCompletedService, times(1)).findById(completedId);
    }

    @Test
    void testListVerifiedAndPending() {
        when(taskUserCompletedService.listVerified()).thenReturn(List.of(completedDto));
        when(taskUserCompletedService.listPending()).thenReturn(List.of(completedDto));

        given().when().get("/completed-task/verified").then().statusCode(200).body("$", hasSize(1));
        given().when().get("/completed-task/pending").then().statusCode(200).body("$", hasSize(1));

        verify(taskUserCompletedService, times(1)).listVerified();
        verify(taskUserCompletedService, times(1)).listPending();
    }

    @Test
    void testListByUserAndTask() {
        when(taskUserCompletedService.listByUser(userId)).thenReturn(List.of(completedDto));
        when(taskUserCompletedService.listByTask(taskId)).thenReturn(List.of(completedDto));
        when(taskUserCompletedService.listByUserAndVerified(userId, true)).thenReturn(List.of(completedDto));
        when(taskUserCompletedService.listByUserAndVerified(userId, false)).thenReturn(List.of(completedDto));
        when(taskUserCompletedService.listByTaskAndVerified(taskId, true)).thenReturn(List.of(completedDto));
        when(taskUserCompletedService.listByTaskAndVerified(taskId, false)).thenReturn(List.of(completedDto));

        given().when().get("/completed-task/user/" + userId).then().statusCode(200).body("$", hasSize(1));
        given().when().get("/completed-task/user/" + userId + "/verified").then().statusCode(200).body("$", hasSize(1));
        given().when().get("/completed-task/user/" + userId + "/pending").then().statusCode(200).body("$", hasSize(1));
        given().when().get("/completed-task/task/" + taskId).then().statusCode(200).body("$", hasSize(1));
        given().when().get("/completed-task/task/" + taskId + "/verified").then().statusCode(200).body("$", hasSize(1));
        given().when().get("/completed-task/task/" + taskId + "/pending").then().statusCode(200).body("$", hasSize(1));

        verify(taskUserCompletedService, times(1)).listByUser(userId);
        verify(taskUserCompletedService, times(1)).listByTask(taskId);
        verify(taskUserCompletedService, times(1)).listByUserAndVerified(userId, true);
        verify(taskUserCompletedService, times(1)).listByUserAndVerified(userId, false);
        verify(taskUserCompletedService, times(1)).listByTaskAndVerified(taskId, true);
        verify(taskUserCompletedService, times(1)).listByTaskAndVerified(taskId, false);
    }

    @Test
    void testCreateCompletedTask() {
        when(taskUserCompletedService.createCompletedTask(any(TaskUserCompletedRequestDto.class))).thenReturn(completedDto);

        given()
                .contentType("application/json")
                .body("{\"taskId\":\"" + taskId + "\",\"userId\":\"" + userId + "\",\"verified\":false}")
                .when()
                .post("/completed-task")
                .then()
                .statusCode(201)
                .body("id", equalTo(completedId.toString()));

        verify(taskUserCompletedService, times(1)).createCompletedTask(any(TaskUserCompletedRequestDto.class));
    }

    @Test
    void testVerifyCompletedTaskNewAndLegacyRoutes() {
        when(taskUserCompletedService.verifyTaskCompleted(completedId)).thenReturn(completedDto);

        given()
                .when()
                .put("/completed-task/" + completedId + "/verify")
                .then()
                .statusCode(200)
                .body("id", equalTo(completedId.toString()));

        given()
                .when()
                .put("/completed-task/" + completedId)
                .then()
                .statusCode(200)
                .body("id", equalTo(completedId.toString()));

        verify(taskUserCompletedService, times(2)).verifyTaskCompleted(completedId);
    }

    @Test
    void testDeleteCompletedTask() {
        doNothing().when(taskUserCompletedService).deleteCompletedTask(completedId);

        given()
                .when()
                .delete("/completed-task/" + completedId)
                .then()
                .statusCode(204);

        verify(taskUserCompletedService, times(1)).deleteCompletedTask(completedId);
    }
}

