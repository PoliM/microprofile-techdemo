package ch.ocram.microprofile.techdemo.it;

import org.apache.cxf.jaxrs.provider.jsrjsonp.JsrJsonpProvider;
import org.junit.Test;

import javax.json.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import static org.assertj.core.api.Assertions.assertThat;

public class PropertiesResourceIT {

    @Test
    public void testGetProperties() {
        String port = System.getProperty("liberty.test.port");
        if (port == null) {
            port = "9501";
        }
        String url = "http://localhost:" + port + "/frontend/rest";

        Client client = ClientBuilder.newClient();
        client.register(JsrJsonpProvider.class);

        WebTarget target = client.target(url).path("/systemproperties");
        Response response = target.request().get();

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());

        JsonObject obj = response.readEntity(JsonObject.class);

        assertThat(obj.getString("os.name")).isEqualTo(System.getProperty("os.name"));

        response.close();
    }
}
