package br.com.avsistems.resource;

import br.com.avsistems.dto.request.GiftsRedemptionRequestDto;
import br.com.avsistems.dto.response.GiftsRedemptionResponseDto;
import br.com.avsistems.service.GiftsRedemptionService;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.UUID;

@Path("/gifts-redemptions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GiftsRedemptionResource {

    @Inject
    GiftsRedemptionService giftsRedemptionService;

    @GET
    public Response listAll() {
        return Response.ok(giftsRedemptionService.listAll()).build();
    }

    @GET
    @Path("/pending")
    public Response listPending() {
        return Response.ok(giftsRedemptionService.listPending()).build();
    }

    @GET
    @Path("/user/{userId}")
    public Response listByUser(@PathParam("userId") UUID userId) {
        return Response.ok(giftsRedemptionService.listByUser(userId)).build();
    }

    @POST
    public Response createRedemption(GiftsRedemptionRequestDto dto) {
        GiftsRedemptionResponseDto response = giftsRedemptionService.createRedemption(dto);
        return Response.status(Response.Status.CREATED).entity(response).build();
    }

    @PUT
    @Path("/{id}/validate")
    public Response validateRedemption(@PathParam("id") UUID id) {
        return Response.ok(giftsRedemptionService.validateRedemption(id)).build();
    }

    @PUT
    @Path("/{id}/cancel")
    public Response cancelRedemption(@PathParam("id") UUID id) {
        return Response.ok(giftsRedemptionService.cancelRedemption(id)).build();
    }
}

