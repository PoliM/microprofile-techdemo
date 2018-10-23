package ch.ocram.microprofile.techdemo.frontend;

import org.eclipse.microprofile.opentracing.Traced;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.enterprise.context.Dependent;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@SuppressWarnings("CdiManagedBeanInconsistencyInspection")
@Dependent
@RegisterRestClient
@RegisterProvider(SomeApplicationExceptionMapper.class)
@Path("/malicious")
@Traced
public interface BackendClient {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    Response getSome(@QueryParam("delay") int delay,
                     @QueryParam("throwSystemException") boolean throwSystemException,
                     @QueryParam("throwApplicationException") boolean throwApplicationException)
            throws SomeApplicationException;
}
