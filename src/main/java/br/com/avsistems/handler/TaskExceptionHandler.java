package br.com.avsistems.handler;

import br.com.avsistems.dto.response.ErrorResponse;
import br.com.avsistems.exceptions.TaskExceptions;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.time.LocalDateTime;

@Provider
public class TaskExceptionHandler implements ExceptionMapper<TaskExceptions> {

    @Override
    public Response toResponse(TaskExceptions exception){
        Response.Status status = Response.Status.BAD_REQUEST;

        if(exception.getMessage().contains("não encontrada")) {
            status = Response.Status.NOT_FOUND;
        }

        ErrorResponse error = new ErrorResponse(
                exception.getMessage(),
                status.getStatusCode(),
                LocalDateTime.now());

        return Response.status(status)
                .entity(error)
                .build();
    }
}
