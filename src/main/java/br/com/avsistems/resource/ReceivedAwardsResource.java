package br.com.avsistems.resource;

import br.com.avsistems.dto.request.ReceivedAwardsMultipartForm;
import br.com.avsistems.dto.response.ReceivedAwardsResponseDto;
import br.com.avsistems.service.ReceivedAwardsService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.UUID;

@Path("/received-awards")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ReceivedAwardsResource {

    @Inject
    ReceivedAwardsService receivedAwardsService;

    @GET
    public Response listAll() {
        return Response.ok(receivedAwardsService.listAll()).build();
    }

    @GET
    @Path("/name/{name}")
    public Response findByName(@PathParam("name") String name) {
        return Response.ok(receivedAwardsService.findByName(name)).build();
    }

    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") UUID id) {
        return Response.ok(receivedAwardsService.findById(id)).build();
    }

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response create(@BeanParam ReceivedAwardsMultipartForm form) {
        ReceivedAwardsResponseDto responseDto = receivedAwardsService.create(form);
        return Response.status(Response.Status.CREATED).entity(responseDto).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response update(@PathParam("id") UUID id, @BeanParam ReceivedAwardsMultipartForm form) {
        ReceivedAwardsResponseDto responseDto = receivedAwardsService.update(id, form);
        return Response.ok(responseDto).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") UUID id) {
        receivedAwardsService.delete(id);
        return Response.noContent().build();
    }
}
