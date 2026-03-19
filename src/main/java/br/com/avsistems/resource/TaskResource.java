package br.com.avsistems.resource;

import br.com.avsistems.dto.request.TaskRequestDto;
import br.com.avsistems.dto.response.TaskResponseDto;
import br.com.avsistems.service.TaskService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.UUID;

@Path("/task")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TaskResource {

    @Inject
    TaskService taskService;

    @POST
    public Response createTask(TaskRequestDto taskRequestDto){
        TaskResponseDto taskResponse = taskService.createTask(taskRequestDto);
        return Response.status(Response.Status.CREATED).entity(taskResponse).build();
    }

    @GET
    public Response listAllTasks(){
        return Response.ok(taskService.listAllTasks()).build();
    }

    @GET
    @Path("/name/{name}")
    public Response findByName(String name){
        return Response.ok(taskService.findByName(name)).build();
    }

    @GET
    @Path("/{id}")
    public Response findById(UUID id){
        return Response.ok(taskService.findById(id)).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateTask(UUID id, TaskRequestDto taskRequestDto){
        return Response.ok(taskService.updateTask(id, taskRequestDto)).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteTask(UUID id){
        taskService.deleteTask(id);
        return Response.noContent().build();
    }

}
