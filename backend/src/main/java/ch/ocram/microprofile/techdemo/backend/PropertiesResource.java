package ch.ocram.microprofile.techdemo.backend;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Properties;

@Path("/systemproperties")
public class PropertiesResource {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @APIResponse(
            responseCode = "200",
            description = "JVM system properties of that service.",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Properties.class)))
    @Operation(
            summary = "Get JVM system properties for that service")
    public JsonObject getProperties() {

        JsonObjectBuilder builder = Json.createObjectBuilder();

        System.getProperties()
                .entrySet()
                .stream()
                .forEach(entry -> builder.add((String) entry.getKey(),
                        (String) entry.getValue()));

        return builder.build();
    }
}
