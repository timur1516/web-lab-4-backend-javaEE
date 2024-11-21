package ru.timur.web4_backend.service;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import ru.timur.web4_backend.dao.UserDAO;
import ru.timur.web4_backend.dao.UserSessionDAO;
import ru.timur.web4_backend.entity.UserEntity;
import ru.timur.web4_backend.entity.UserSessionEntity;
import ru.timur.web4_backend.exception.SessionNotFoundException;
import ru.timur.web4_backend.util.JWTUtil;

import java.util.Date;

@Stateless
public class UserSessionServiceImpl implements UserSessionService{
    @EJB
    private UserSessionDAO userSessionDAO;
    @EJB
    private JWTUtil jwtUtil;

    @Override
    public  String startSession(UserEntity user) {
        String token = jwtUtil.generateToken(user.getUsername(), user.getId());
        UserSessionEntity userSessionEntity = UserSessionEntity
                .builder()
                .token(token)
                .lastActivity(new Date())
                .build();

        userSessionDAO.saveSession(userSessionEntity);

        return token;
    }

    @Override
    public void endSession(String token) {
        userSessionDAO.deleteSessionByToken(token);
    }

    @Override
    public void updateLastActivity(String token) throws SessionNotFoundException {
        UserSessionEntity userSessionEntity = userSessionDAO
                .getSessionByToken(token)
                .orElseThrow(() -> new SessionNotFoundException("Session with token " + token + " does not exists"));
        userSessionEntity.setLastActivity(new Date());
        userSessionDAO.updateSession(userSessionEntity);
    }

    @Override
    public String getRefreshedTokenIfExpired(String token) {
        if (!jwtUtil.isTokenExpired(token)) {
            if (jwtUtil.getExpiresAtFromToken(token).getTime() - System.currentTimeMillis() < 5 * 60 * 1000) {
                UserSessionEntity userSessionEntity = userSessionDAO
                        .getSessionByToken(token)
                        .orElse(null);
                if(userSessionEntity == null) return null;
                String newToken = jwtUtil.generateToken(jwtUtil.getUsernameFromToken(token), jwtUtil.getUserIdFromToken(token));
                userSessionEntity.setToken(newToken);
                userSessionDAO.updateSession(userSessionEntity);
                return newToken;
            }
        }
        return null;
    }
}
