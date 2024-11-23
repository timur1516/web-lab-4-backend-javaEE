package ru.timur.web4_backend.controller;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import lombok.extern.slf4j.Slf4j;
import ru.timur.web4_backend.dto.PointRequestDTO;
import ru.timur.web4_backend.dto.PointResponseDTO;
import ru.timur.web4_backend.exception.UserNotFoundException;
import ru.timur.web4_backend.service.UserService;
import ru.timur.web4_backend.util.UserPrincipals;

import java.util.List;

@Slf4j
@Path("/main")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserController {
    @EJB
    private UserService userService;

    @Context
    private SecurityContext securityContext;

    @GET
    @Path("/get-points")
    public Response getPoints() {
        UserPrincipals userPrincipals = (UserPrincipals) securityContext.getUserPrincipal();
        List<PointResponseDTO> points = userService.getAllPoints(userPrincipals.getUserId());
        return Response.ok(points).build();
    }

    @POST
    @Path("/check-point")
    public Response checkpoint(PointRequestDTO point) {
        UserPrincipals userPrincipals = (UserPrincipals) securityContext.getUserPrincipal();
        try {
            PointResponseDTO response = userService.addPoint(point, userPrincipals.getUserId());
            return Response.ok(response).build();
        } catch (UserNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }
}
