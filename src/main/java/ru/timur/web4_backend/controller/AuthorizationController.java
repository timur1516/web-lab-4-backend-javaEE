package ru.timur.web4_backend.controller;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import lombok.extern.slf4j.Slf4j;
import ru.timur.web4_backend.dto.CredentialsDTO;
import ru.timur.web4_backend.dto.TokenDTO;
import ru.timur.web4_backend.dto.UserDTO;
import ru.timur.web4_backend.exception.*;
import ru.timur.web4_backend.service.AuthorizationService;
import ru.timur.web4_backend.util.UserPrincipals;

@Slf4j
@Path("/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthorizationController {
    @EJB
    private AuthorizationService authorizationService;

    @Context
    private SecurityContext securityContext;

    @POST
    @Path("/signup")
    public Response singUp(UserDTO user) {
        try {
            CredentialsDTO token = authorizationService.registerUser(user.getUsername(), user.getPassword());
            log.info("Successfully registered user: {}", user.getUsername());
            return Response.ok(token).build();
        } catch (UsernameExistsException | InvalidAuthorizationDataException e) {
            log.info("Registration failed for user {}: {}", user.getUsername(), e.getMessage());
            return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
        }
    }

    @POST
    @Path("/login")
    public Response logIn(UserDTO user) {
        try {
            CredentialsDTO token = authorizationService.authenticateUser(user.getUsername(), user.getPassword());
            log.info("Successfully authenticated user: {}", user.getUsername());
            return Response.ok(token).build();
        } catch (InvalidAuthorizationDataException e) {
            log.info("Authentication failed for user {}: {}", user.getUsername(), e.getMessage());
            return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
        } catch (UserNotFoundException e) {
            log.info("Authentication failed for user {}: {}", user.getUsername(), e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (AuthenticationException e) {
            log.info("Authentication failed for user {}: {}", user.getUsername(), e.getMessage());
            return Response.status(Response.Status.UNAUTHORIZED).entity(e.getMessage()).build();
        }
    }

    @POST
    @Path("/logout")
    public Response logOut() {
        UserPrincipals userPrincipal = (UserPrincipals) securityContext.getUserPrincipal();
        authorizationService.logout(userPrincipal.getToken());
        log.info("Successfully logged out user with id: {}", userPrincipal.getUserId());
        return Response.ok("Logout successfully").build();
    }

    @POST
    @Path("/refresh-token")
    public Response refreshToken(TokenDTO token) {
        try {
            TokenDTO newToken = authorizationService.getRefreshedToken(token);
            log.debug("Successfully refreshed token: {} -> {}", token.getToken(), newToken.getToken());
            return Response.ok(newToken).build();
        } catch (SessionTimeoutException | SessionNotFoundException e) {
            log.info("Unable to refresh token: {}", e.getMessage());
            return Response.status(Response.Status.UNAUTHORIZED).entity(e.getMessage()).build();
        }
    }
}
