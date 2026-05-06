package br.com.avsistems.handler;

import br.com.avsistems.dto.response.ErrorResponse;
import br.com.avsistems.exceptions.AuthException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.time.LocalDateTime;

@Provider
public class AuthExceptionHandler implements ExceptionMapper<AuthException> {
    @Override
    public Response toResponse(AuthException exception) {
        // Para erro de login, o padrão de mercado é 401 (Unauthorized)
        Response.Status status = Response.Status.UNAUTHORIZED;

        ErrorResponse error = new ErrorResponse(
                exception.getMessage(),
                status.getStatusCode(),
                LocalDateTime.now()
        );

        return Response.status(status)
                .entity(error)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}