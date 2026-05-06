package br.com.avsistems.config;

import jakarta.annotation.security.PermitAll;
import jakarta.ws.rs.OPTIONS;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("/_preflight")
public class PreflightResource {

    @OPTIONS
    @Path("/")
    @PermitAll
    public Response preflight() {
        return Response.ok().build();
    }
}