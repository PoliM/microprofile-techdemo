package ch.ocram.microprofile.techdemo.backend;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class SomeApplicationExceptionMapper implements ExceptionMapper<SomeApplicationException> {
    @Override
    public Response toResponse(SomeApplicationException exception) {
        return Response.status(Response.Status.PAYMENT_REQUIRED).build();
    }
}
