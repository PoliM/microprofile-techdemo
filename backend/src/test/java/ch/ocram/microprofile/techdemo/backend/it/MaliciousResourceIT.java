package ch.ocram.microprofile.techdemo.backend.it;

import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

public class MaliciousResourceIT extends IntegrationTestBase {

    @Test
    public void testOk() {
        Response response = target.path("/malicious").request().get();
        response.close();

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
    }

    @Test
    public void testSystemException() {
        Response response = target.path("/malicious").queryParam("throwSystemException", "true").request().get();
        response.close();

        assertThat(response.getStatus()).isEqualTo(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
    }

    @Test
    public void testApplicationException() {
        Response response = target.path("/malicious").queryParam("throwApplicationException", "true").request().get();
        response.close();

        assertThat(response.getStatus()).isEqualTo(Response.Status.PAYMENT_REQUIRED.getStatusCode());
    }

    @Test
    public void testDelay() {
        long startTs = System.currentTimeMillis();
        Response response = target.path("/malicious").queryParam("delay", "500").request().get();
        long diff = System.currentTimeMillis() - startTs;
        response.close();

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(diff).isCloseTo(500L, within(50L));
    }

    @Test
    public void testHealth() {
        assertThat(healthCheck()).isTrue();

        Response response = target.path("/malicious").request().post(Entity.text("3"));
        response.close();
        assertThat(response.getStatus()).isEqualTo(Response.Status.ACCEPTED.getStatusCode());

        assertThat(healthCheck()).isFalse();
        assertThat(healthCheck()).isFalse();
        assertThat(healthCheck()).isFalse();

        assertThat(healthCheck()).isTrue();
    }

}
