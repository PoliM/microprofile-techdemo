package ch.ocram.microprofile.techdemo.frontend;

import ch.ocram.microprofile.techdemo.frontend.api.SystempropertiesApi;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.core.Response;

public class PropertiesResource implements SystempropertiesApi {

    public Response getSystemProperties() {

        JsonObjectBuilder builder = Json.createObjectBuilder();

        System.getProperties()
                .forEach((key, value) -> builder.add((String) key,
                        (String) value));

        return Response.ok(builder.build()).build();
    }
}
