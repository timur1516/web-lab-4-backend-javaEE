package ru.timur.web4_backend.resources;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import ru.timur.web4_backend.model.HelloWorld;

@Path("/hello-world")
public class HelloResource {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public HelloWorld hello() {
        return new HelloWorld("Hello World");
    }
}
