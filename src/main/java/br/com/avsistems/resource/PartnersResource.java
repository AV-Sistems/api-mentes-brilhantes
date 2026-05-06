package br.com.avsistems.resource;

import br.com.avsistems.dto.request.PartnerMultipartForm;
import br.com.avsistems.dto.response.PartnersResponseDto;
import br.com.avsistems.service.PartnersService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.UUID;

@Path("/partners")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PartnersResource {

    @Inject
    PartnersService partnersService;

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response createPartner(@BeanParam PartnerMultipartForm form) {
        PartnersResponseDto response = partnersService.createPartner(form);
        return Response.status(Response.Status.CREATED)
                .entity(response)
                .build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response updatePartners(@PathParam("id") UUID id, @BeanParam PartnerMultipartForm form) {
        PartnersResponseDto response = partnersService.updatePartner(id, form);
        return Response.ok(response).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deletePartner(@PathParam("id") UUID id) {
        partnersService.deletePartner(id);
        return Response.noContent().build();
    }

    @GET
    public Response findAll() {
        return Response.ok(partnersService.findAll()).build();
    }

    @GET
    @Path("/validity/{state}/{city}")
    public Response FindByValidityAndStateAndCity(@PathParam("state") String state, @PathParam("city") String city) {
        return Response.ok(partnersService.FindByValidityAndStateAndCity(state, city)).build();
    }

    @GET
    @Path("/validity")
    public Response FindAllByValidity() {
        return Response.ok(partnersService.findAllByValidity()).build();
    }

    @Path("/admin")
    public static class AdminResource {

        @GET
        @RolesAllowed("admin")
        @Produces(MediaType.TEXT_PLAIN)
        public String admin() {
            return "Admin";
        }
    }
}
