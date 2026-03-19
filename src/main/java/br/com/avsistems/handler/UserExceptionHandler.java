package br.com.avsistems.handler;

import br.com.avsistems.dto.response.ErrorResponse;
import br.com.avsistems.exceptions.UserExceptions;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.time.LocalDateTime;

@Provider
public class UserExceptionHandler implements ExceptionMapper<UserExceptions> {
    @Override
    public Response toResponse(UserExceptions exception) {
        Response.Status status = Response.Status.BAD_REQUEST; // Padrão 400

        if (exception.getMessage().contains("Usuário não encontrado.")) {
            status = Response.Status.NOT_FOUND;
        }

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
