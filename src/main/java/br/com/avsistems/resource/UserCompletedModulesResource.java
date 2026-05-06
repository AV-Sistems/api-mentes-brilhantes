package br.com.avsistems.resource;

import br.com.avsistems.dto.request.UserCompletedModulesRequestDto;
import br.com.avsistems.dto.response.UserCompletedModulesResponseDto;
import br.com.avsistems.service.UserCompletedModulesService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.UUID;

@Path("/user-completed-modules")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserCompletedModulesResource {

    @Inject
    UserCompletedModulesService userCompletedModulesService;

    @GET
    public Response listAll() {
        return Response.ok(userCompletedModulesService.listAll()).build();
    }

    @GET
    @Path("/user/{userId}")
    public Response findByUserId(@PathParam("userId") UUID userId) {
        return Response.ok(userCompletedModulesService.findByUserId(userId)).build();
    }

    @GET
    @Path("/module/{completedModuleId}")
    public Response findByCompletedModuleId(@PathParam("completedModuleId") UUID completedModuleId) {
        return Response.ok(userCompletedModulesService.findByCompletedModuleId(completedModuleId)).build();
    }

    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") UUID id) {
        return Response.ok(userCompletedModulesService.findById(id)).build();
    }

    @POST
    public Response create(UserCompletedModulesRequestDto requestDto) {
        UserCompletedModulesResponseDto responseDto = userCompletedModulesService.create(requestDto);
        return Response.status(Response.Status.CREATED).entity(responseDto).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") UUID id) {
        userCompletedModulesService.delete(id);
        return Response.noContent().build();
    }
}

