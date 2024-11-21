package ru.timur.web4_backend.service;

import ru.timur.web4_backend.exception.AuthenticationException;
import ru.timur.web4_backend.exception.InvalidAuthorizationDataException;
import ru.timur.web4_backend.exception.UserNotFoundException;
import ru.timur.web4_backend.exception.UsernameExistsException;

public interface AuthorizationService {
    String registerUser(String username, String password) throws UsernameExistsException, InvalidAuthorizationDataException;
    String authenticateUser(String username, String password) throws InvalidAuthorizationDataException, UserNotFoundException, AuthenticationException;
}
