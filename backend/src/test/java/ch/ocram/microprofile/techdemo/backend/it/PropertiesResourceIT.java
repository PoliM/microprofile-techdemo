package ch.ocram.microprofile.techdemo.backend.it;

import org.junit.Test;

import javax.json.JsonObject;
import javax.ws.rs.core.Response;

import static org.assertj.core.api.Assertions.assertThat;

public class PropertiesResourceIT extends IntegrationTestBase {

    @Test
    public void testGetProperties() {
        Response response = target.path("/systemproperties").request().get();
        JsonObject obj = response.readEntity(JsonObject.class);
        response.close();

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());


        assertThat(obj.getString("os.name")).isEqualTo(System.getProperty("os.name"));
    }
}
