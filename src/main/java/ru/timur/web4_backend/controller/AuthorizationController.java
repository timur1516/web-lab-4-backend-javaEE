package ru.timur.web4_backend.controller;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import ru.timur.web4_backend.dto.TokenDTO;
import ru.timur.web4_backend.dto.UserDTO;
import ru.timur.web4_backend.exception.*;
import ru.timur.web4_backend.service.AuthorizationService;
import ru.timur.web4_backend.util.UserPrincipals;

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
            String token = authorizationService.registerUser(user.getUsername(), user.getPassword());
            return Response.ok(new TokenDTO(token)).build();
        } catch (UsernameExistsException | InvalidAuthorizationDataException e) {
            return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
        }
    }

    @POST
    @Path("/login")
    public Response logIn(UserDTO user) {
        try {
            String token = authorizationService.authenticateUser(user.getUsername(), user.getPassword());
            return Response.ok(new TokenDTO(token)).build();
        } catch (InvalidAuthorizationDataException e) {
            return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
        } catch (UserNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (AuthenticationException e) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(e.getMessage()).build();
        }
    }

    @POST
    @Path("/logout")
    public Response logOut() {
        UserPrincipals userPrincipal = (UserPrincipals) securityContext.getUserPrincipal();
        authorizationService.logout(userPrincipal.getToken());
        return Response.ok("Logout successfully").build();
    }

    @POST
    @Path("/checktoken")
    public Response checkToken(TokenDTO tokenDTO) {
        if(authorizationService.isValidToken(tokenDTO.getToken()))
            return Response.ok().build();
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }
}
