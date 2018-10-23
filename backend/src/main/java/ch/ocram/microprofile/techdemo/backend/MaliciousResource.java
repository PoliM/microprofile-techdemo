package ch.ocram.microprofile.techdemo.backend;

import org.eclipse.microprofile.health.Health;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.opentracing.Traced;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/malicious")
@ApplicationScoped
public class MaliciousResource {

    @Inject
    @Health
    private HealthProbe healthProbe;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Timed(description = "Time needed for the malicious method.")
    @Traced
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

    @POST
    @Timed(description = "Time needed to set the countdown.")
    @APIResponse(
            responseCode = "202",
            description = "The operation went through successfully")
    @APIResponse(
            responseCode = "422",
            description = "The countdown needs to be > 0")
    @Operation(
            summary = "Sets the number of healtchecks that will fail.")
    public Response setHealthProbeCountdown(@RequestBody(description = "The number of times the health check will fail") int countdown) {

        if (countdown > 0) {
            this.healthProbe.setCountdown(countdown);
            return Response.accepted().build();
        }

        return Response.status(422).build(); // Unprocessable entity
    }
}
