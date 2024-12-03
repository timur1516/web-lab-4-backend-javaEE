package ru.timur.web4_backend.service;

import lombok.extern.java.Log;
import ru.timur.web4_backend.dto.CredentialsDTO;
import ru.timur.web4_backend.dto.TokenDTO;
import ru.timur.web4_backend.exception.*;

public interface AuthorizationService {
    CredentialsDTO registerUser(String username, String password) throws UsernameExistsException, InvalidAuthorizationDataException;
    CredentialsDTO authenticateUser(String username, String password) throws InvalidAuthorizationDataException, UserNotFoundException, AuthenticationException;
    CredentialsDTO findGoodOrNew(Long userId);
    void logout(String token);
    TokenDTO getRefreshedToken(TokenDTO token) throws SessionTimeoutException, SessionNotFoundException;
}
