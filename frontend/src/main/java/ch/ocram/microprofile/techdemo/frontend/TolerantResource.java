package ch.ocram.microprofile.techdemo.frontend;

import ch.ocram.microprofile.techdemo.frontend.api.TolerantApi;
import org.eclipse.microprofile.faulttolerance.*;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ThreadLocalRandom;

@ApplicationScoped
public class TolerantResource implements TolerantApi {

    // Ok, this is a total hack an for demonstration purpose only!
    private static int COUNTER = 0;
    private static long LAST_UPDATE;

    @SuppressWarnings("CdiInjectionPointsInspection")
    @Inject
    @RestClient
    private BackendClient backendClient;

    @Override
    @Retry
    public Response tolerantRetry() {
        try {
            // Reset the counter if the last call was a bit in the past
            if (System.currentTimeMillis() - LAST_UPDATE > 500) {
                COUNTER = 0;
            }

            COUNTER++;
            LAST_UPDATE = System.currentTimeMillis();

            backendClient.getSome(0, ThreadLocalRandom.current().nextDouble() > 0.2,  false);
            return Response.ok("Counter: " + COUNTER, MediaType.TEXT_PLAIN_TYPE).build();
        } catch (SomeApplicationException e) {
            return Response.status(Response.Status.PAYMENT_REQUIRED).build();
        }
    }

    @Timeout(1000)
    @Fallback(fallbackMethod = "fallbackForTolerantTimeout")
    @Override
    public Response tolerantTimeout(Integer delay) {
        try {
            int delayValue = delay == null ? 0 : delay.intValue();
            backendClient.getSome(delayValue, false, false);
            return Response.ok().build();
        } catch (SomeApplicationException e) {
            return Response.status(Response.Status.PAYMENT_REQUIRED).build();
        }
    }

    public Response fallbackForTolerantTimeout(Integer delay) {
        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }


    @CircuitBreaker(requestVolumeThreshold = 4, failureRatio = 0.75, successThreshold = 2, delay = 10, delayUnit = ChronoUnit.SECONDS)
    @Fallback(fallbackMethod = "fallbackForToleranCircuitBreaker")
    @Override
    public Response tolerantCircuitBreaker(Boolean fail) {
        try {
            COUNTER++;
            backendClient.getSome(0, false, fail);
            return Response.ok("Counter: " + COUNTER, MediaType.TEXT_PLAIN_TYPE).build();
        } catch (SomeApplicationException e) {
            throw new IllegalStateException();
        }
    }

    public Response fallbackForToleranCircuitBreaker(Boolean fail) {
        return Response
                .status(Response.Status.SERVICE_UNAVAILABLE)
                .entity("Counter: " + COUNTER)
                .type(MediaType.TEXT_PLAIN_TYPE)
                .build();
    }


    @Override
    public Response withoutBulkhead() {
        try {
            return backendClient.getSome(200, false, false);
        } catch (SomeApplicationException e) {
            return Response.status(Response.Status.PAYMENT_REQUIRED).build();
        }
    }

    @Bulkhead
    @Override
    public Response withBulkhead() {
        try {
            return backendClient.getSome(200, false, false);
        } catch (SomeApplicationException e) {
            return Response.status(Response.Status.PAYMENT_REQUIRED).build();
        }
    }
}
