package br.com.avsistems.resource;

import br.com.avsistems.dto.request.UserReceivedAwardsRequestDto;
import br.com.avsistems.dto.response.UserReceivedAwardsResponseDto;
import br.com.avsistems.service.UserReceivedAwardsService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.UUID;

@Path("/user-received-awards")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserReceivedAwardsResource {

    @Inject
    UserReceivedAwardsService userReceivedAwardsService;

    @GET
    public Response listAll() {
        return Response.ok(userReceivedAwardsService.listAll()).build();
    }

    @GET
    @Path("/user/{userId}")
    public Response findByUserId(@PathParam("userId") UUID userId) {
        return Response.ok(userReceivedAwardsService.findByUserId(userId)).build();
    }

    @GET
    @Path("/award/{receivedAwardId}")
    public Response findByReceivedAwardId(@PathParam("receivedAwardId") UUID receivedAwardId) {
        return Response.ok(userReceivedAwardsService.findByReceivedAwardId(receivedAwardId)).build();
    }

    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") UUID id) {
        return Response.ok(userReceivedAwardsService.findById(id)).build();
    }

    @POST
    public Response create(UserReceivedAwardsRequestDto requestDto) {
        UserReceivedAwardsResponseDto responseDto = userReceivedAwardsService.create(requestDto);
        return Response.status(Response.Status.CREATED).entity(responseDto).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") UUID id) {
        userReceivedAwardsService.delete(id);
        return Response.noContent().build();
    }
}

