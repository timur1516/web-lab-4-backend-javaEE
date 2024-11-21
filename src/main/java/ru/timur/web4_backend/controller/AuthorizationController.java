package ru.timur.web4_backend.controller;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import ru.timur.web4_backend.dto.TokenDTO;
import ru.timur.web4_backend.dto.UserDTO;
import ru.timur.web4_backend.exception.InvalidAuthorizationDataException;
import ru.timur.web4_backend.exception.UserNotFoundException;
import ru.timur.web4_backend.exception.UsernameExistsException;
import ru.timur.web4_backend.exception.AuthenticationException;
import ru.timur.web4_backend.service.AuthorizationService;

@Path("/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthorizationController {
    @EJB
    private AuthorizationService authorizationService;

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
}
