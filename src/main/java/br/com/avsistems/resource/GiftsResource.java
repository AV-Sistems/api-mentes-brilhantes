package br.com.avsistems.resource;

import br.com.avsistems.dto.request.GiftsRequestDto;
import br.com.avsistems.service.GiftsService;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.UUID;

@Path("/gifts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GiftsResource {

    @Inject
    GiftsService giftsService;

    @GET
    public Response listAll() {
        return Response.ok(giftsService.listAll()).build();
    }

    @GET
    @Path("/available")
    public Response listAvailable() {
        return Response.ok(giftsService.listAvailable()).build();
    }

    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") UUID id) {
        return Response.ok(giftsService.findById(id)).build();
    }

    @POST
    public Response createGift(GiftsRequestDto dto) {
        return Response.status(Response.Status.CREATED)
                .entity(giftsService.createGift(dto))
                .build();
    }

    @PUT
    @Path("/{id}")
    public Response updateGift(@PathParam("id") UUID id, GiftsRequestDto dto) {
        return Response.ok(giftsService.updateGift(id, dto)).build();
    }

    @PATCH
    @Path("/{id}/status")
    public Response toggleStatus(@PathParam("id") UUID id) {
        return Response.ok(giftsService.toggleStatus(id)).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteGift(@PathParam("id") UUID id) {
        giftsService.deleteGift(id);
        return Response.noContent().build();
    }
}

