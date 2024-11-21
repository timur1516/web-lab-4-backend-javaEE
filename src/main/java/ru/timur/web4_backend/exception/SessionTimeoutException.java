package ru.timur.web4_backend.exception;

public class SessionTimeoutException extends Exception {
    public SessionTimeoutException(String message) {
        super(message);
    }
}
