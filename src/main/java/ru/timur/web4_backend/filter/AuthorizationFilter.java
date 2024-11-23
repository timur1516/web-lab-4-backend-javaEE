package ru.timur.web4_backend.filter;

import jakarta.annotation.Priority;
import jakarta.ejb.EJB;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;
import ru.timur.web4_backend.exception.SessionNotFoundException;
import ru.timur.web4_backend.exception.SessionTimeoutException;
import ru.timur.web4_backend.service.UserSessionService;
import ru.timur.web4_backend.util.JWTUtil;
import ru.timur.web4_backend.util.UserPrincipals;

import java.io.IOException;
import java.security.Principal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthorizationFilter implements ContainerRequestFilter {
    @EJB
    private JWTUtil jwtUtil;

    @EJB
    private UserSessionService userSessionService;

    private static final Set<String> SKIP_PATHS = new HashSet<>(Arrays.asList(
            "/auth/signup",
            "/auth/login",
            "/auth/refresh-token"
    ));

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String path = requestContext.getUriInfo().getPath();
        log.info("Received request with path: {}", path);
        if (SKIP_PATHS.contains(path)) {
            return;
        }

        String authHeader = requestContext.getHeaderString("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.info("Authorization header not found");
            requestContext.abortWith(Response
                    .status(Response.Status.UNAUTHORIZED)
                    .entity("Authorization header must be provided")
                    .build());
            return;
        }

        String token = authHeader.substring("Bearer".length()).trim();
        log.info("Received token: {}", token);

        try {
            userSessionService.validateToken(token);
        } catch (SessionNotFoundException e) {
            log.info("Session not found for token: {}", token);
            requestContext.abortWith(Response
                    .status(Response.Status.UNAUTHORIZED)
                    .entity("Invalid token")
                    .build());
            return;
        } catch (SessionTimeoutException e) {
            log.info("Session timeout for token: {}", token);
            requestContext.abortWith(Response
                    .status(Response.Status.UNAUTHORIZED)
                    .entity("Login timeout expired")
                    .build());
            return;
        }

        //TODO: remove jwtUtil from this module
        Long userId = jwtUtil.getUserId(token);
        requestContext.setSecurityContext(new SecurityContext() {
            @Override
            public Principal getUserPrincipal() {
                return new UserPrincipals(userId, token);
            }

            @Override
            public boolean isUserInRole(String s) {
                return false;
            }

            @Override
            public boolean isSecure() {
                return requestContext.getSecurityContext().isSecure();
            }

            @Override
            public String getAuthenticationScheme() {
                return "Bearer";
            }
        });
    }
}
