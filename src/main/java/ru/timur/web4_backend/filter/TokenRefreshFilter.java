package ru.timur.web4_backend.filter;

import jakarta.ejb.EJB;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;
import ru.timur.web4_backend.service.UserSessionService;

import java.io.IOException;

@Provider
public class TokenRefreshFilter implements ContainerResponseFilter {
    @EJB
    private UserSessionService userSessionService;

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        String authHeader = requestContext.getHeaderString("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring("Bearer".length()).trim();
            String newToken = userSessionService.getRefreshedTokenIfExpired(token);
            if (newToken != null) {
                responseContext.getHeaders().add("Authorization", "Bearer " + newToken);
            }
        }
    }
}
