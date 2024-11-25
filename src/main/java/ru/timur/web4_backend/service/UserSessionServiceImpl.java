package ru.timur.web4_backend.service;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import ru.timur.web4_backend.dao.UserSessionDAO;
import ru.timur.web4_backend.dto.CredentialsDTO;
import ru.timur.web4_backend.dto.TokenDTO;
import ru.timur.web4_backend.entity.UserEntity;
import ru.timur.web4_backend.entity.UserSessionEntity;
import ru.timur.web4_backend.exception.SessionNotFoundException;
import ru.timur.web4_backend.exception.SessionTimeoutException;
import ru.timur.web4_backend.util.JWTUtil;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Stateless
public class UserSessionServiceImpl implements UserSessionService {
    @EJB
    private UserSessionDAO userSessionDAO;
    @EJB
    private JWTUtil jwtUtil;

    private final long ACCESS_TOKEN_VALIDITY_MINUTES = 30;
    private final long REFRESH_TOKEN_VALIDITY_HOURS = 5;

    @Override
    public CredentialsDTO startSession(UserEntity user) {
        String accessToken = jwtUtil.generateToken(user.getId(), Instant.now().plus(ACCESS_TOKEN_VALIDITY_MINUTES, ChronoUnit.MINUTES));
        String refreshToken = jwtUtil.generateToken(user.getId(), Instant.now().plus(REFRESH_TOKEN_VALIDITY_HOURS, ChronoUnit.HOURS));
        UserSessionEntity userSessionEntity = UserSessionEntity
                .builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

        userSessionDAO.saveSession(userSessionEntity);

        return new CredentialsDTO(accessToken, refreshToken);
    }

    @Override
    public void endSession(String token) {
        userSessionDAO.deleteSessionByAccessToken(token);
    }

    @Override
    public void validateToken(String token) throws SessionNotFoundException, SessionTimeoutException {
        if (jwtUtil.isExpired(token))
            throw new SessionTimeoutException("Session with token " + token + " is expired");
        if (userSessionDAO.getSessionByAccessToken(token).isEmpty())
            throw new SessionNotFoundException("Session with token " + token + " does not exists");
    }

    @Override
    public TokenDTO refreshToken(String token) throws SessionNotFoundException, SessionTimeoutException {
        if (jwtUtil.isExpired(token)) {
            userSessionDAO.deleteSessionByRefreshToken(token);
            throw new SessionTimeoutException("Session with token " + token + " is expired");
        }
        UserSessionEntity userSession = userSessionDAO
                .getSessionByRefreshToken(token)
                .orElseThrow(() -> new SessionNotFoundException("Session with token " + token + " does not exists"));

        String newAccessToken = jwtUtil.generateToken(jwtUtil.getUserId(token), Instant.now().plus(ACCESS_TOKEN_VALIDITY_MINUTES, ChronoUnit.MINUTES));
        userSession.setAccessToken(newAccessToken);
        userSessionDAO.updateSession(userSession);
        return new TokenDTO(newAccessToken);
    }
}
