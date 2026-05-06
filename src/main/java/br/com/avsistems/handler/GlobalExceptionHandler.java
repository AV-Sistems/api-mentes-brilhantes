package br.com.avsistems.handler;

import br.com.avsistems.dto.response.ErrorResponse;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;

import java.time.LocalDateTime;

@Provider
public class GlobalExceptionHandler implements ExceptionMapper<Exception> {

    private static final Logger LOG = Logger.getLogger(GlobalExceptionHandler.class);

    @Override
    public Response toResponse(Exception exception) {
        LOG.error("Exception não tratada", exception);

        Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;
        String message = "Erro interno do servidor";

        if (exception instanceof NotFoundException) {
            status = Response.Status.NOT_FOUND;
            message = "Recurso não encontrado";
        } else if (exception instanceof BadRequestException) {
            status = Response.Status.BAD_REQUEST;
            message = exception.getMessage() != null ? exception.getMessage() : "Requisição inválida";
        } else if (exception instanceof IllegalArgumentException) {
            status = Response.Status.BAD_REQUEST;
            message = exception.getMessage() != null ? exception.getMessage() : "Argumento inválido";
        } else if (exception.getMessage() != null) {
            message = exception.getMessage();
        }

        ErrorResponse error = new ErrorResponse(
                message,
                status.getStatusCode(),
                LocalDateTime.now()
        );

        return Response.status(status)
                .entity(error)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}

