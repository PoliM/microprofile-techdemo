package ch.ocram.microprofile.techdemo.backend.it;

import org.apache.cxf.jaxrs.provider.jsrjsonp.JsrJsonpProvider;
import org.junit.After;
import org.junit.Before;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

public class IntegrationTestBase {

    WebTarget target;
    private Client client;
    private String url;

    @Before
    public void setup() {
        String port = System.getProperty("liberty.test.port");
        if (port == null) {
            port = "9502";
        }
        url = "http://localhost:" + port;

        client = ClientBuilder.newClient();
        client.register(JsrJsonpProvider.class);
        target = client.target(url + "/backend/rest");
    }

    @After
    public void teardown() {
        client.close();
    }

    boolean healthCheck() {
        Response response = client.target(url + "/health").request().get();
        response.close();
        return response.getStatus() == 200;
    }
}
