package br.com.avsistems.resource;

import br.com.avsistems.dto.request.LiveWithYouDto;
import br.com.avsistems.service.LiveWithYouService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.UUID;

@Path("/live-with-you")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LiveWithYouResource {

    @Inject
    LiveWithYouService liveWithYouService;

    @GET
    @Path("/{id}")
    public Response findAllByUserId(@PathParam("id") UUID id){
        return Response.ok(liveWithYouService.ListByUserId(id)).build();
    }

    @POST
    @Path("/{id}")
    public Response createParent(@PathParam("id") UUID id, List<LiveWithYouDto> dtos){
        return Response.ok(liveWithYouService.create(id, dtos)).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteParent(@PathParam("id") UUID id){
        liveWithYouService.delete(id);
        return Response.noContent().build();
    }
}
