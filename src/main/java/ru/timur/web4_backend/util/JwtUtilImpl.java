package ru.timur.web4_backend.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import jakarta.ejb.Singleton;
import jakarta.ejb.Stateless;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Stateless
public class JwtUtilImpl implements JWTUtil {
    @Override
    public String generateToken(Long userId, Instant expiresAt) {
        //TODO: Add secure property file with secret key
        String secretKey = "oeRaYY7Wo24sDqKSX3IM9ASGmdGPmkTd9jo1QTy4b7P9Ze5_9hKolVX8xNrQDcNRfVEdTZNOuOyqEGhXEbdJI-ZQ19k_o9MI0y3eZN2lp9jow55FfXMiINEdt1XR85VipRLSOkT6kSpzs2x-jbLDiz9iFVzkd81YKxMgPA7VfZeQUm4n-mOmnWMaVX30zGFU4L3oPBctYKkl4dYfqYWqRNfrgPJVi5DGFjywgxx0ASEiJHtV72paI3fDR2XwlSkyhhmY-ICjCRmsJN4fX1pdoL8a18-aQrvyu4j0Os6dVPYIoPvvY0SAZtWYKHfM15g7A3HD4cVREf9cUsprCRK93w";
        return JWT.create()
                .withClaim("userId", userId)
                .withExpiresAt(expiresAt)
                .sign(Algorithm.HMAC256(secretKey));
    }

    @Override
    public Long getUserId(String token) {
        try {
            return JWT.decode(token).getClaim("userId").asLong();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    @Override
    public boolean isExpired(String token) {
        try {
            Date expirationTime = JWT.decode(token).getExpiresAt();
            return expirationTime != null && expirationTime.before(new Date());
        } catch (JWTDecodeException e) {
            return true;
        }
    }
}
