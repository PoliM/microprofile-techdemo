package ch.ocram.microprofile.techdemo.backend;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Info;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("rest")
@OpenAPIDefinition(info = @Info(title = "Backend services", version = "1.0.0"))
public class BackendApplication extends Application {
}
