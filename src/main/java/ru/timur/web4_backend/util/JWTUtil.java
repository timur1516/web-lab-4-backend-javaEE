package ru.timur.web4_backend.util;

import java.time.Instant;

public interface JWTUtil {
    String generateToken(Long userId, Instant expiresAt);
    Long getUserId(String token);
    boolean isExpired(String token);
}
