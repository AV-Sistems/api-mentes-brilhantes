package br.com.avsistems.resource;

import br.com.avsistems.service.UserService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.UUID;

@Path("/admin")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AdminResources {

    @Inject
    UserService userService;

    @GET
    @RolesAllowed("ADMIN")
    public String admin() {
        return "Admin";
    }

    @PUT
    @Path("/alter-active-user/{id}")
    @RolesAllowed("ADMIN")
    public Response alterActiveUser(@PathParam("id") UUID id){
        return Response.ok(userService.alterActivation(id)).build();
    }

    @GET
    @Path("/inactive")
    @RolesAllowed("ADMIN")
    public Response findAllInactiveUser(){
        return Response.ok(userService.findAllInactiveUser()).build();
    }


}
