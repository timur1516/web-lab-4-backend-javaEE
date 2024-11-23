package ru.timur.web4_backend.service;

import ru.timur.web4_backend.dto.CredentialsDTO;
import ru.timur.web4_backend.dto.TokenDTO;
import ru.timur.web4_backend.entity.UserEntity;
import ru.timur.web4_backend.exception.SessionNotFoundException;
import ru.timur.web4_backend.exception.SessionTimeoutException;

public interface UserSessionService {
    CredentialsDTO startSession(UserEntity user);
    void endSession(String token);
    void validateToken(String token) throws SessionNotFoundException, SessionTimeoutException;
    TokenDTO refreshToken(String token) throws SessionNotFoundException, SessionTimeoutException;
}
