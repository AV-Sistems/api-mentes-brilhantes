package br.com.avsistems.resource;

import br.com.avsistems.dto.request.UserImageMultipartForm;
import br.com.avsistems.dto.request.UserUpdateAdressDto;
import br.com.avsistems.dto.request.UserUpdateDto;
import br.com.avsistems.service.UserService;
import br.com.avsistems.type.UserType;
import io.quarkus.security.Authenticated;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.IOException;
import java.time.LocalDate;
import java.util.UUID;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    @Inject
    UserService userService;

    @GET
    @PermitAll //anotação para permitir que qualquer pessoa acesse essa rota
    public Response findAll(){
        return Response.ok(userService.findAllActiveUser()).build();
    }

    @GET
    @Path("/ranking")
    @PermitAll
    public Response findRanking() {
        return Response.ok(userService.findRankingByTotalPoints()).build();
    }

    @GET
    @Path("/{state}/{city}")
    public Response findByStateAndCity(@PathParam("state") String state, @PathParam("city") String city){
        return Response.ok(userService.findByStateAndCity(state, city)).build();
    }

    @GET
    @Path("/name/{name}")
    public Response findByName(@PathParam("name") String name){
        return Response.ok(userService.findByNameContaining(name)).build();
    }

    @GET
    @Path("/id/{id}")
    public Response findById(@PathParam("id") UUID id){
        return Response.ok(userService.findById(id)).build();
    }

    @GET
    @Path("/education-instituition/{instituition}")
    public Response findByEducationInstituitionContaining(@PathParam("instituition") String instituition) {
        return Response.ok(userService.findByEducationInstituitionContaining(instituition)).build();
    }

    @GET
    @Path("/user-type/{userType}")
    public Response findByUserTypeContaining(@PathParam("userType") String userType) {
        return Response.ok(userService.findByUserTypeContaining(userType)).build();
    }

    @GET
    @Path("/mentes-edition/{edition}")
    public Response findByMentesEditionContaining(@PathParam("edition") String edition) {
        return Response.ok(userService.findByMentesEditionContaining(edition)).build();
    }

    @GET
    @Path("/date-of-birth/{date}")
    public Response findByDateOfBirth(@PathParam("date") LocalDate date) {
        return Response.ok(userService.findByDateOfBirth(date)).build();
    }

    @GET
    @Path("/inactive-user")
    public Response findInactiveUser(){
        return Response.ok(userService.findAllInactiveUser()).build();
    }

    @PUT
    @Path("/adrress/{id}")
    @Authenticated //apenas usuários autenticados podem acessar essa rota
    public Response updateUser(@PathParam("id") UUID id, UserUpdateAdressDto userUpdateAdressDto){
        return Response.ok(userService.updateUserAddress(id, userUpdateAdressDto)).build();
    }

    @PUT
    @Path("/{id}")
    @Authenticated
    public Response updateUser(@PathParam("id") UUID id, UserUpdateDto userUpdateDto){
        return Response.ok(userService.updateUser(id, userUpdateDto)).build();
    }

    @PUT
    @Path("/{id}/image")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Authenticated
    public Response uploadUserImage(@PathParam("id") UUID id, @BeanParam UserImageMultipartForm form) throws IOException {
        return Response.ok(userService.addUserImage(id, form)).build();
    }


    @PUT
    @Path("/active-user/{id}")
    @Authenticated
    public Response alterActiveUser(@PathParam("id") UUID id){
        return Response.ok(userService.alterActivation(id)).build();
    }

    @PUT
    @Path("/user-type/{id}")
    @Authenticated
    public Response toggleUserType(@PathParam("id") UUID id){
        return Response.ok(userService.toggleUserType(id)).build();
    }

    @DELETE
    @Path("/{id}")
    @Authenticated
    public Response deleteUser(@PathParam("id") UUID id){
        userService.deleteUser(id);
        return Response.noContent().build();//retorna 204
    }
}
