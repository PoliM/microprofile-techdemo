package ch.ocram.microprofile.techdemo.frontend;

import org.eclipse.microprofile.rest.client.ext.ResponseExceptionMapper;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

@Provider
public class SomeApplicationExceptionMapper implements ResponseExceptionMapper<SomeApplicationException> {

    @Override
    public boolean handles(int status, MultivaluedMap<String, Object> headers) {
        return status == Response.Status.PAYMENT_REQUIRED.getStatusCode();
    }

    @Override
    public SomeApplicationException toThrowable(Response response) {
        return new SomeApplicationException();
    }
}