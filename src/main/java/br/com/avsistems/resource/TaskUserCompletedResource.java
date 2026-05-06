package br.com.avsistems.resource;

import br.com.avsistems.dto.request.TaskUserCompletedRequestDto;
import br.com.avsistems.dto.response.TaskUserCompletedResponseDto;
import br.com.avsistems.service.TaskUserCompletedService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.UUID;

@Path("/completed-task")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TaskUserCompletedResource {

    @Inject
    TaskUserCompletedService taskUserCompletedService;

    @GET
    public Response listAll(){
        return Response.ok(taskUserCompletedService.listAll()).build();
    }

    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") UUID id){
        return Response.ok(taskUserCompletedService.findById(id)).build();
    }

    @GET
    @Path("/verified")
    public Response listVerified(){
        return Response.ok(taskUserCompletedService.listVerified()).build();
    }

    @GET
    @Path("/pending")
    public Response listPending(){
        return Response.ok(taskUserCompletedService.listPending()).build();
    }

    @GET
    @Path("/user/{userId}")
    public Response listByUser(@PathParam("userId") UUID userId){
        return Response.ok(taskUserCompletedService.listByUser(userId)).build();
    }

    @GET
    @Path("/user/{userId}/verified")
    public Response listByUserVerified(@PathParam("userId") UUID userId){
        return Response.ok(taskUserCompletedService.listByUserAndVerified(userId, true)).build();
    }

    @GET
    @Path("/user/{userId}/pending")
    public Response listByUserPending(@PathParam("userId") UUID userId){
        return Response.ok(taskUserCompletedService.listByUserAndVerified(userId, false)).build();
    }

    @GET
    @Path("/task/{taskId}")
    public Response listByTask(@PathParam("taskId") UUID taskId){
        return Response.ok(taskUserCompletedService.listByTask(taskId)).build();
    }

    @GET
    @Path("/task/{taskId}/verified")
    public Response listByTaskVerified(@PathParam("taskId") UUID taskId){
        return Response.ok(taskUserCompletedService.listByTaskAndVerified(taskId, true)).build();
    }

    @GET
    @Path("/task/{taskId}/pending")
    public Response listByTaskPending(@PathParam("taskId") UUID taskId){
        return Response.ok(taskUserCompletedService.listByTaskAndVerified(taskId, false)).build();
    }

    @POST
    public Response create(TaskUserCompletedRequestDto dto){
        TaskUserCompletedResponseDto response = taskUserCompletedService.createCompletedTask(dto);
        return Response.status(Response.Status.CREATED).entity(response).build();
    }

    @PUT
    @Path("/{id}/verify")
    public Response verifyCompletedTask(@PathParam("id") UUID id){
        return Response.ok(taskUserCompletedService.verifyTaskCompleted(id)).build();
    }

    @PUT
    @Path("/{id}")
    public Response verifyCompletedTaskLegacy(@PathParam("id") UUID id){
        return Response.ok(taskUserCompletedService.verifyTaskCompleted(id)).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteCompletedTask(@PathParam("id") UUID id){
        taskUserCompletedService.deleteCompletedTask(id);
        return Response.noContent().build();
    }
}
