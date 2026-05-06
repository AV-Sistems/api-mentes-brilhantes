package br.com.avsistems.resource;

import br.com.avsistems.dto.request.UserCreateDto;
import br.com.avsistems.dto.request.UserCredentials;
import br.com.avsistems.dto.response.LoginResponseDto;
import br.com.avsistems.dto.response.UserResponseDto;
import br.com.avsistems.entity.UserEntity;
import br.com.avsistems.exceptions.AuthException;
import br.com.avsistems.service.UserService;
import io.quarkus.elytron.security.common.BcryptUtil;
import io.smallrye.jwt.build.Jwt;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.time.Duration;
import java.util.Set;

@Path("/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthResource {

    @Inject
    @ConfigProperty(name = "mp.jwt.verify.issuer")
    String issuer;

    @Inject
    UserService userService;

    @POST
    @Path("/login")
    @PermitAll
    public Response login(UserCredentials credentials){
        String email = credentials.email.toLowerCase().trim();

        UserEntity user = UserEntity.find("LOWER(email)", email).firstResult();

        if(user != null && user.active == false){
            throw new AuthException("Usuário inativo. Aguarde a sua ativação por favor.");
        }

        if(user == null || !BcryptUtil.matches(credentials.password, user.password)){
            throw new AuthException("E-mail ou senha incorretos.");
        }

        String token = Jwt.issuer(issuer)
                .upn(user.email)
                .claim("id", user.id) // 🔥 ESSENCIAL
                .groups(Set.of(user.userType.toString()))
                .expiresIn(Duration.ofHours(8))
                .sign();

        System.out.println("User: " + user);

        return Response.ok(
                new LoginResponseDto(
                        token,
                        new UserResponseDto(user)
                )
        ).build();
    }

    @POST
    @Path("/register")
    @PermitAll
    public Response createUser(UserCreateDto userCreateDto){
        UserResponseDto userResponse = userService.createUser(userCreateDto);
        return Response.status(Response.Status.CREATED).entity(userResponse).build();
    }
}