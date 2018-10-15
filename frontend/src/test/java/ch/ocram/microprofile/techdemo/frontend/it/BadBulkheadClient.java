package ch.ocram.microprofile.techdemo.frontend.it;

import org.apache.cxf.jaxrs.provider.jsrjsonp.JsrJsonpProvider;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.util.stream.IntStream;

public class BadBulkheadClient {

    private static Client client;
    private static String url;
    private static boolean useBulkhead = true;

    public static void main(String[] args) throws InterruptedException {

        String port = System.getProperty("liberty.test.port");
        if (port == null) {
            port = "9501";
        }
        url = "http://localhost:" + port + "/frontend/rest";

        client = ClientBuilder.newClient();
        client.register(JsrJsonpProvider.class);

        new Thread(BadBulkheadClient::syspropsCall, "sysprop").start();

        IntStream.range(0, 100).forEach(i -> {
            new Thread(BadBulkheadClient::bulkCall, "bulk"+i).start();
        });

        Thread.sleep(10000);
        System.exit(0);
    }

    private static void syspropsCall() {
        while (true) {
            long start = System.currentTimeMillis();
            WebTarget target = client.target(url).path("/systemproperties");
            Response response = target.request().get();
            long diff = System.currentTimeMillis() - start;
            System.out.println(Thread.currentThread().getName() + " -> " + diff + " / " + response.getStatus());
            response.close();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // ok
            }
        }
    }

    private static void bulkCall() {
        while (true) {
            long start = System.currentTimeMillis();
            WebTarget target = client.target(url).path(useBulkhead ? "/tolerant/withbulkhead" : "/tolerant/withoutbulkhead");
            Response response = target.request().get();
            long diff = System.currentTimeMillis() - start;
            System.out.println(Thread.currentThread().getName() + " -> " + diff + " / " + response.getStatus());
            response.close();
        }
    }
}
