package ru.timur.web4_backend.dao;

import ru.timur.web4_backend.entity.UserSessionEntity;

import java.util.Optional;

public interface UserSessionDAO {
    void saveSession(UserSessionEntity userSessionEntity);
    void updateSession(UserSessionEntity userSessionEntity);
    void deleteSessionByToken(String token);
    Optional<UserSessionEntity> getSessionByToken(String token);
}
