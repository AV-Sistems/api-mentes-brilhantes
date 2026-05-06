package br.com.avsistems.exceptions;

/**
 * Exceção lançada quando um recurso não é encontrado.
 * Mapeia para HTTP 404.
 */
public class NotFoundException extends ApplicationException {
    public NotFoundException(String message) {
        super(message);
    }
}

