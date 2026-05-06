package br.com.avsistems.resource;

import br.com.avsistems.dto.request.CompletedModulesRequestDto;
import br.com.avsistems.dto.response.CompletedModulesResponseDto;
import br.com.avsistems.service.CompletedModulesService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.UUID;

@Path("/completed-modules")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CompletedModulesResource {

    @Inject
    CompletedModulesService completedModulesService;

    @GET
    public Response listAll() {
        return Response.ok(completedModulesService.listAll()).build();
    }

    @GET
    @Path("/name/{name}")
    public Response findByName(@PathParam("name") String name) {
        return Response.ok(completedModulesService.findByName(name)).build();
    }

    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") UUID id) {
        return Response.ok(completedModulesService.findById(id)).build();
    }

    @POST
    public Response create(CompletedModulesRequestDto requestDto) {
        CompletedModulesResponseDto responseDto = completedModulesService.create(requestDto);
        return Response.status(Response.Status.CREATED).entity(responseDto).build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") UUID id, CompletedModulesRequestDto requestDto) {
        CompletedModulesResponseDto responseDto = completedModulesService.update(id, requestDto);
        return Response.ok(responseDto).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") UUID id) {
        completedModulesService.delete(id);
        return Response.noContent().build();
    }
}

