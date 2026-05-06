package br.com.avsistems.resource;

import br.com.avsistems.dto.request.TaskRequestDto;
import br.com.avsistems.dto.response.TaskResponseDto;
import br.com.avsistems.service.TaskService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.IOException;
import java.util.UUID;

@Path("/task")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TaskResource {

    @Inject
    TaskService taskService;

    @POST
    public Response createTask(TaskRequestDto form) throws IOException {
        TaskResponseDto taskResponse = taskService.createTask(form);
        return Response.status(Response.Status.CREATED).entity(taskResponse).build();
    }

    @GET
    public Response listAllTasks(){
        return Response.ok(taskService.listAllTasks()).build();
    }

    @GET
    @Path("/name/{name}")
    public Response findByName(@PathParam("name") String name){
        return Response.ok(taskService.findByName(name)).build();
    }

    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") UUID id){
        return Response.ok(taskService.findById(id)).build();
    }

    @GET
    @Path("/status-active")
    public Response findActiveTasks(){
        return Response.ok(taskService.findByStatusActive()).build();
    }

    @GET
    @Path("/status-active/user")
    public Response findActiveTasksForUser(){
        return Response.ok(taskService.findByStatusActiveForUser()).build();
    }

    @GET
    @Path("/type/{type}")
    public Response findByType(@PathParam("type") String type){
        return Response.ok(taskService.findByType(type)).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateTask(@PathParam("id") UUID id, TaskRequestDto form) throws IOException {
        return Response.ok(taskService.updateTask(id, form)).build();
    }

    @PUT
    @Path("/alter-status/{id}")
    public Response alterStatusTask(@PathParam("id") UUID id){
        return Response.ok(taskService.alterStatusTask(id)).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteTask(@PathParam("id") UUID id){
        taskService.deleteTask(id);
        return Response.noContent().build();
    }
}
