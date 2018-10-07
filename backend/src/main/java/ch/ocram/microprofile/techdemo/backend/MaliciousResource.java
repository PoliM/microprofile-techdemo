package ch.ocram.microprofile.techdemo.backend;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/malicious")
public class MaliciousResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @APIResponse(
            responseCode = "200",
            description = "The operation went through successfully")
    @APIResponse(
            responseCode = "500",
            description = "When you set throwSystemException")
    @APIResponse(
            responseCode = "402",
            description = "When you set throwApplicationException")
    @Operation(
            summary = "Behaves maliciously - on your command")
    public Response getSome(@Parameter(description = "Delay the operation from returning for the amount of milliseconds")
                            @QueryParam("delay") int delay,

                            @Parameter(description = "Force the operation to throw a NullPointerException")
                            @QueryParam("throwSystemException") boolean throwSystemException,

                            @Parameter(description = "Force the operation to throw an application exception")
                            @QueryParam("throwApplicationException") boolean throwApplicationException)
            throws SomeApplicationException {

        if (delay > 0) {
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                // ok
            }
        }

        if (throwSystemException) {
            throw new NullPointerException("You said so");
        }

        if (throwApplicationException) {
            throw new SomeApplicationException();
        }

        return Response.ok().build();
    }
}
