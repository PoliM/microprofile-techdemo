package ch.ocram.microprofile.techdemo.frontend;

import ch.ocram.microprofile.techdemo.frontend.api.TolerantApi;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;

@ApplicationScoped
public class TolerantResource implements TolerantApi {

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
}
