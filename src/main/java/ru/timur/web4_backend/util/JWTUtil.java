package ru.timur.web4_backend.util;

import java.util.Date;

public interface JWTUtil {
    String generateToken(String username, Long userId);
    String getUsernameFromToken(String token);
    Long getUserIdFromToken(String token);
    boolean isTokenExpired(String token);
    Date getExpiresAtFromToken(String token);
}
