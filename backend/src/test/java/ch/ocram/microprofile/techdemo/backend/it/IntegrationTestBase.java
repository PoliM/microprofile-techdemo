package ch.ocram.microprofile.techdemo.backend.it;

import org.apache.cxf.jaxrs.provider.jsrjsonp.JsrJsonpProvider;
import org.junit.After;
import org.junit.Before;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

public class IntegrationTestBase {

    private Client client;
    WebTarget target;

    @Before
    public void setup(){
        String port = System.getProperty("liberty.test.port");
        if (port == null) {
            port = "9501";
        }
        String url = "http://localhost:" + port + "/backend/rest";

        client = ClientBuilder.newClient();
        client.register(JsrJsonpProvider.class);
        target = client.target(url);
    }

    @After
    public void teardown() {
        client.close();
    }
}
