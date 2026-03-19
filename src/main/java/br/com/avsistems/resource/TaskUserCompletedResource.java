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

    @POST
    public Response create(TaskUserCompletedRequestDto dto){
        TaskUserCompletedResponseDto response = taskUserCompletedService.createCompletedTask(dto);
        return Response.status(Response.Status.CREATED).entity(response).build();
    }

    @PUT
    @Path("/{id}")
    public Response uptadeCompletedTask(UUID id){
        return Response.ok(taskUserCompletedService.updateTaskCompleted(id)).build();
    }
}
