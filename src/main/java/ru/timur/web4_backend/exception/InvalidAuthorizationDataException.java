package ru.timur.web4_backend.exception;

public class InvalidAuthorizationDataException extends Exception {
    public InvalidAuthorizationDataException(String message) {
        super(message);
    }
}
