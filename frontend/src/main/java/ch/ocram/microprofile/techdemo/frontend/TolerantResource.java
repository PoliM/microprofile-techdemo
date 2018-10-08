package ch.ocram.microprofile.techdemo.frontend;

import ch.ocram.microprofile.techdemo.frontend.api.TolerantApi;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;

@ApplicationScoped
public class TolerantResource implements TolerantApi {

    @SuppressWarnings("CdiInjectionPointsInspection")
    @Inject
    @RestClient
    private BackendClient backendClient;

    @Override
    public Response tolerantRetry() {
        try {
            backendClient.getSome(0, false, false);
            return Response.ok().build();
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
}
