package ch.ocram.microprofile.techdemo.frontend;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.opentracing.ClientTracingRegistrar;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;

@ApplicationScoped
public class BackendClientWithTracing {

    @Inject
    @ConfigProperty(name = "ch.ocram.microprofile.techdemo.frontend.BackendClient/mp-rest/url")
    private String baseUrl;

    Response getSome(int delay,
                     boolean throwSystemException,
                     boolean throwApplicationException)
            throws SomeApplicationException {


        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        clientBuilder = ClientTracingRegistrar.configure(clientBuilder);

        String customUrlString = baseUrl;

        Response response = clientBuilder.build()
                .target(customUrlString)
                .path("malicious")
                .queryParam("delay", delay)
                .queryParam("throwSystemException", throwSystemException)
                .queryParam("throwApplicationException", throwApplicationException)
                .request().get();

        return response;
    }
}
