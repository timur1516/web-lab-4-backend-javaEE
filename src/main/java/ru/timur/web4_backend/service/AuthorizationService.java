package ru.timur.web4_backend.service;

import ru.timur.web4_backend.exception.*;

public interface AuthorizationService {
    String registerUser(String username, String password) throws UsernameExistsException, InvalidAuthorizationDataException;
    String authenticateUser(String username, String password) throws InvalidAuthorizationDataException, UserNotFoundException, AuthenticationException;
    void logout(String token);
    boolean isValidToken(String token);
}
