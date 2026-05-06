package br.com.avsistems.exceptions;

/**
 * Exceção lançada quando os dados da requisição são inválidos.
 * Mapeia para HTTP 400.
 */
public class BadRequestException extends ApplicationException {
    public BadRequestException(String message) {
        super(message);
    }
}

