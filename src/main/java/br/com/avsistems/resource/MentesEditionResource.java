package br.com.avsistems.resource;

import br.com.avsistems.dto.request.MentesEditionRequestDto;
import br.com.avsistems.service.MentesEditionService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.UUID;

@Path("/mentes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MentesEditionResource {

    @Inject
    MentesEditionService mentesEditionService;

    @POST
    @RolesAllowed("ADMIN")
    public Response createMentesEdition(MentesEditionRequestDto mentesDto){
        return Response.status(Response.Status.CREATED)
                .entity(mentesEditionService.createMentesEdition(mentesDto))
                .build();
    }

    @GET
    public Response listAll(){
        return Response.ok(mentesEditionService.listAllMentesEdition()).build();
    }

    @GET
    @Path("/{state}/{city}")
    public Response findByStateAndCity(String state, String city){
        return Response.ok(mentesEditionService.findByStateAndCity(state, city)).build();
    }

    @GET
    @Path("/title/{title}")
    public Response findByName(String title){
        return Response.ok(mentesEditionService.findByName(title)).build();
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    public Response updateMentesEdition(MentesEditionRequestDto mentesDto, @PathParam("id") UUID id){
        return Response.ok(mentesEditionService.updateMentes(id, mentesDto)).build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    public Response deleteMentesEdition(@PathParam("id") UUID id){
        mentesEditionService.deleteMentes(id);
        return Response.noContent().build(); //204 se ok
    }

}
