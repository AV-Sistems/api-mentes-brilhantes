package br.com.avsistems.handler;

import br.com.avsistems.dto.response.ErrorResponse;
import br.com.avsistems.exceptions.ApplicationException;
import br.com.avsistems.exceptions.BadRequestException;
import br.com.avsistems.exceptions.ConflictException;
import br.com.avsistems.exceptions.NotFoundException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;

import java.time.LocalDateTime;

@Provider
public class ApplicationExceptionHandler implements ExceptionMapper<ApplicationException> {

    private static final Logger LOG = Logger.getLogger(ApplicationExceptionHandler.class);

    @Override
    public Response toResponse(ApplicationException exception) {
        LOG.errorf("ApplicationException capturada: %s", exception.getMessage(), exception);

        Response.Status status = Response.Status.BAD_REQUEST;

        // Usar instanceof para detectar tipo de exceção (mais robusto que string matching)
        if (exception instanceof NotFoundException) {
            status = Response.Status.NOT_FOUND;
        } else if (exception instanceof ConflictException) {
            status = Response.Status.CONFLICT;
        } else if (exception instanceof BadRequestException) {
            status = Response.Status.BAD_REQUEST;
        } else if (exception.getMessage() != null && exception.getMessage().contains("Erro ao")) {
            status = Response.Status.INTERNAL_SERVER_ERROR;
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
