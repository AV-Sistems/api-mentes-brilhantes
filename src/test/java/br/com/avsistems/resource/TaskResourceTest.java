package br.com.avsistems.resource;

import br.com.avsistems.dto.request.TaskRequestDto;
import br.com.avsistems.dto.response.GiftsResponseDto;
import br.com.avsistems.dto.response.TaskResponseDto;
import br.com.avsistems.exceptions.BadRequestException;
import br.com.avsistems.exceptions.NotFoundException;
import br.com.avsistems.service.TaskService;
import br.com.avsistems.type.GiftsStatus;
import br.com.avsistems.type.TasksStatus;
import br.com.avsistems.type.TasksType;
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
@DisplayName("TaskResource - Testes Unitários")
public class TaskResourceTest {

    @InjectMock
    TaskService taskService;

    private UUID testTaskId;
    private TaskResponseDto testTaskDto;

    @BeforeEach
    void setUp() {
        testTaskId = UUID.randomUUID();
        GiftsResponseDto gifts = new GiftsResponseDto(
                UUID.randomUUID(),
                "Brinde Teste",
                50,
                false,
                10,
                GiftsStatus.ACTIVE,
                null,
                null
        );
        testTaskDto = new TaskResponseDto(
                testTaskId,
                "Aprender Java",
                10,
                TasksStatus.ACTIVE,
                gifts,
                TasksType.NORMAL,
                30
        );
    }

    @Test
    @DisplayName("POST /task - Deve criar tarefa com sucesso")
    void testCreateTaskSuccess() {
        when(taskService.createTask(any(TaskRequestDto.class))).thenReturn(testTaskDto);

        given()
                .contentType("application/json")
                .body("""
                        {
                          "name":"Aprender Java",
                          "tasksPoints":10,
                          "tasksStatus":"ACTIVE",
                          "tasksType":"NORMAL",
                          "recurrenceDays":30
                        }
                        """)
                .when().post("/task")
                .then()
                .statusCode(201)
                .body("name", equalTo("Aprender Java"))
                .body("tasksPoints", equalTo(10))
                .body("tasksStatus", equalTo("ACTIVE"));

        verify(taskService, times(1)).createTask(any(TaskRequestDto.class));
    }

    @Test
    @DisplayName("POST /task - Deve retornar 400 quando recurrenceDays é inválido")
    void testCreateTaskInvalidData() {
        when(taskService.createTask(any(TaskRequestDto.class)))
                .thenThrow(new BadRequestException("O campo recurrenceDays não pode ser negativo."));

        given()
                .contentType("application/json")
                .body("{" +
                        "\"name\":\"Aprender Java\"," +
                        "\"tasksPoints\":10," +
                        "\"tasksStatus\":\"ACTIVE\"," +
                        "\"tasksType\":\"NORMAL\"," +
                        "\"recurrenceDays\":-1}")
                .when().post("/task")
                .then()
                .statusCode(400)
                .body("message", containsString("recurrenceDays"));
    }

    @Test
    @DisplayName("GET /task - Deve retornar lista de tarefas")
    void testListAllTasksSuccess() {
        when(taskService.listAllTasks()).thenReturn(List.of(testTaskDto));

        given()
                .when().get("/task")
                .then()
                .statusCode(200)
                .body("$", hasSize(1))
                .body("[0].name", equalTo("Aprender Java"));

        verify(taskService, times(1)).listAllTasks();
    }

    @Test
    @DisplayName("GET /task - Deve retornar lista vazia")
    void testListAllTasksEmpty() {
        when(taskService.listAllTasks()).thenReturn(List.of());

        given()
                .when().get("/task")
                .then()
                .statusCode(200)
                .body("$", empty());
    }

    @Test
    @DisplayName("GET /task/name/{name} - Deve retornar tarefas por nome")
    void testFindByNameSuccess() {
        when(taskService.findByName("Aprender")).thenReturn(List.of(testTaskDto));

        given()
                .when().get("/task/name/Aprender")
                .then()
                .statusCode(200)
                .body("$", hasSize(1))
                .body("[0].name", equalTo("Aprender Java"));

        verify(taskService, times(1)).findByName("Aprender");
    }

    @Test
    @DisplayName("GET /task/{id} - Deve retornar tarefa por ID")
    void testFindByIdSuccess() {
        when(taskService.findById(testTaskId)).thenReturn(testTaskDto);

        given()
                .when().get("/task/" + testTaskId)
                .then()
                .statusCode(200)
                .body("id", equalTo(testTaskId.toString()))
                .body("name", equalTo("Aprender Java"));

        verify(taskService, times(1)).findById(testTaskId);
    }

    @Test
    @DisplayName("GET /task/{id} - Deve retornar 404 quando não encontrada")
    void testFindByIdNotFound() {
        when(taskService.findById(testTaskId))
                .thenThrow(new NotFoundException("Tarefa com ID: " + testTaskId + " não encontrada."));

        given()
                .when().get("/task/" + testTaskId)
                .then()
                .statusCode(404)
                .body("message", containsString("não encontrada"));
    }

    @Test
    @DisplayName("GET /task/status-active - Deve retornar tarefas ativas")
    void testFindActiveTasksSuccess() {
        when(taskService.findByStatusActive()).thenReturn(List.of(testTaskDto));

        given()
                .when().get("/task/status-active")
                .then()
                .statusCode(200)
                .body("$", hasSize(1))
                .body("[0].tasksStatus", equalTo("ACTIVE"));

        verify(taskService, times(1)).findByStatusActive();
    }

    @Test
    @DisplayName("GET /task/status-active/user - Deve retornar tarefas ativas para usuário")
    void testFindActiveTasksForUserSuccess() {
        when(taskService.findByStatusActiveForUser()).thenReturn(List.of(testTaskDto));

        given()
                .when().get("/task/status-active/user")
                .then()
                .statusCode(200)
                .body("$", hasSize(1));

        verify(taskService, times(1)).findByStatusActiveForUser();
    }

    @Test
    @DisplayName("GET /task/type/{type} - Deve retornar tarefas por tipo")
    void testFindByTypeSuccess() {
        when(taskService.findByType("NORMAL")).thenReturn(List.of(testTaskDto));

        given()
                .when().get("/task/type/NORMAL")
                .then()
                .statusCode(200)
                .body("$", hasSize(1))
                .body("[0].tasksType", equalTo("NORMAL"));

        verify(taskService, times(1)).findByType("NORMAL");
    }

    @Test
    @DisplayName("PUT /task/{id} - Deve atualizar tarefa com sucesso")
    void testUpdateTaskSuccess() {
        TaskResponseDto updatedDto = new TaskResponseDto(
                testTaskId,
                "Aprender Quarkus",
                15,
                TasksStatus.ACTIVE,
                testTaskDto.gifts(),
                TasksType.NORMAL,
                30
        );
        when(taskService.updateTask(eq(testTaskId), any(TaskRequestDto.class))).thenReturn(updatedDto);

        given()
                .contentType("application/json")
                .body("{" +
                        "\"name\":\"Aprender Quarkus\"," +
                        "\"tasksPoints\":15," +
                        "\"tasksStatus\":\"ACTIVE\"," +
                        "\"tasksType\":\"NORMAL\"," +
                        "\"recurrenceDays\":30}")
                .when().put("/task/" + testTaskId)
                .then()
                .statusCode(200)
                .body("name", equalTo("Aprender Quarkus"));

        verify(taskService, times(1)).updateTask(eq(testTaskId), any(TaskRequestDto.class));
    }

    @Test
    @DisplayName("PUT /task/{id} - Deve retornar 404 quando tarefa não existe")
    void testUpdateTaskNotFound() {
        when(taskService.updateTask(eq(testTaskId), any(TaskRequestDto.class)))
                .thenThrow(new NotFoundException("Tarefa não encontrada"));

        given()
                .contentType("application/json")
                .body("{" +
                        "\"name\":\"Aprender Quarkus\"," +
                        "\"tasksPoints\":15," +
                        "\"tasksStatus\":\"ACTIVE\"," +
                        "\"tasksType\":\"NORMAL\"," +
                        "\"recurrenceDays\":30}")
                .when().put("/task/" + testTaskId)
                .then()
                .statusCode(404)
                .body("message", containsString("não encontrada"));
    }

    @Test
    @DisplayName("PUT /task/alter-status/{id} - Deve alterar status da tarefa")
    void testAlterStatusTaskSuccess() {
        TaskResponseDto inactiveDto = new TaskResponseDto(
                testTaskId,
                testTaskDto.name(),
                testTaskDto.tasksPoints(),
                TasksStatus.INACTIVE,
                testTaskDto.gifts(),
                testTaskDto.tasksType(),
                testTaskDto.recurrenceDays()
        );
        when(taskService.alterStatusTask(testTaskId)).thenReturn(inactiveDto);

        given()
                .when().put("/task/alter-status/" + testTaskId)
                .then()
                .statusCode(200)
                .body("tasksStatus", equalTo("INACTIVE"));

        verify(taskService, times(1)).alterStatusTask(testTaskId);
    }

    @Test
    @DisplayName("DELETE /task/{id} - Deve deletar tarefa com sucesso")
    void testDeleteTaskSuccess() {
        doNothing().when(taskService).deleteTask(testTaskId);

        given()
                .when().delete("/task/" + testTaskId)
                .then()
                .statusCode(204);

        verify(taskService, times(1)).deleteTask(testTaskId);
    }

    @Test
    @DisplayName("DELETE /task/{id} - Deve retornar 404 quando tarefa não existe")
    void testDeleteTaskNotFound() {
        doThrow(new NotFoundException("Tarefa não encontrada")).when(taskService).deleteTask(testTaskId);

        given()
                .when().delete("/task/" + testTaskId)
                .then()
                .statusCode(404)
                .body("message", containsString("não encontrada"));
    }
}
