package br.com.avsistems.exceptions;

/**
 * Exceção lançada quando há um conflito nos dados (ex: duplicado, estado inválido).
 * Mapeia para HTTP 409.
 */
public class ConflictException extends ApplicationException {
    public ConflictException(String message) {
        super(message);
    }
}

