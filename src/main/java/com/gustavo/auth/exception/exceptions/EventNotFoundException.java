package com.gustavo.auth.exception.exceptions;

public class EventNotFoundException extends RuntimeException {

    public EventNotFoundException() { super("Evento não encontrado!"); }

    public EventNotFoundException(String message) { super(message); }
}
