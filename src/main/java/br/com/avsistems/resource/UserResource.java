package br.com.avsistems.resource;

import br.com.avsistems.dto.request.UserCreateDto;
import br.com.avsistems.dto.request.UserUpdateDto;
import br.com.avsistems.dto.response.UserResponseDto;
import br.com.avsistems.service.UserService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.UUID;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    @Inject
    UserService userService;

    @POST
    public Response createUser(UserCreateDto userCreateDto){
        UserResponseDto userResponse = userService.createUser(userCreateDto);
        return Response.status(Response.Status.CREATED).entity(userResponse).build();    }

    @GET
    public Response listAllUsers(){
        return Response.ok(userService.listAllUsers()).build();
    }

    @GET
    @Path("/{id}")
    public Response findById(UUID id){
        return Response.ok(userService.findById(id)).build();
    }

    @GET
    @Path("/email/{email}")
    public Response findByEmail(String email){return Response.ok(userService.findByEmail(email)).build();}

    @PUT
    @Path("/{id}")
    public Response updateUser(UUID id, UserUpdateDto userUpdateDto){
        return Response.ok(userService.updateUser(id, userUpdateDto)).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteUser(UUID id){
        userService.deleteUser(id);
        return Response.noContent().build();//retorna 204
    }
}
