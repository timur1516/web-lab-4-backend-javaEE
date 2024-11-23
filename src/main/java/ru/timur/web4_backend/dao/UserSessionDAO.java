package ru.timur.web4_backend.dao;

import ru.timur.web4_backend.entity.UserSessionEntity;

import java.util.Optional;

public interface UserSessionDAO {
    void saveSession(UserSessionEntity userSessionEntity);
    void updateSession(UserSessionEntity userSessionEntity);
    void deleteSessionByAccessToken(String token);
    void deleteSessionByRefreshToken(String token);
    Optional<UserSessionEntity> getSessionByAccessToken(String token);
    Optional<UserSessionEntity> getSessionByRefreshToken(String token);
}
