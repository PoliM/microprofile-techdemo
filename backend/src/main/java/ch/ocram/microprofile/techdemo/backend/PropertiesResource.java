package ch.ocram.microprofile.techdemo.backend;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("systemproperties")
public class PropertiesResource {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject getProperties() {

        JsonObjectBuilder builder = Json.createObjectBuilder();

        System.getProperties()
                .entrySet()
                .stream()
                .forEach(entry -> builder.add((String)entry.getKey(),
                        (String)entry.getValue()));

        return builder.build();
    }
}
