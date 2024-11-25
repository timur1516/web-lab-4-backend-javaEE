package ru.timur.web4_backend.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Stateless;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.SecretKey;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.Properties;

@Slf4j
@Stateless
public class JwtUtilImpl implements JWTUtil {
    private Key key;
    @PostConstruct
    private void init() {
        Properties properties = new Properties();
        String propertiesPath = System.getenv("JWT_CONFIG_PATH");
        if (propertiesPath == null) {
            log.error("Unable to find JWT property path. Create JWT_CONFIG_PATH system variable");
            return;
        }
        try {
            properties.load(new FileInputStream(propertiesPath));
        } catch (IOException e) {
            log.error("Error loading properties file for JWT", e);
            return;
        }
        String secretKey = properties.getProperty("secretKey");
        if(secretKey == null) {
            log.error("secretKey property was not found in JWT property file");
            return;
        }
        key = Keys.hmacShaKeyFor(secretKey.getBytes());
        log.info("JWT secret key was loaded successfully");
    }

    @Override
    public String generateToken(Long userId, Instant expiresAt) {
        return Jwts
                .builder()
                .claim("userId", userId)
                .expiration(Date.from(expiresAt))
                .signWith(key)
                .compact();
    }

    @Override
    public Long getUserId(String token) {
        try {
            return Jwts
                    .parser()
                    .verifyWith((SecretKey) key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .get("userId", Long.class);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean isExpired(String token) {
        try {
            Date expirationTime = Jwts
                    .parser()
                    .verifyWith((SecretKey) key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getExpiration();
            if(expirationTime == null) return true;
            return expirationTime.before(new Date());
        } catch (Exception e) {
            return true;
        }
    }
}
