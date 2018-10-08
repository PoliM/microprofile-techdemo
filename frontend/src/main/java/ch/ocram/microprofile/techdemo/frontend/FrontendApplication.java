package ch.ocram.microprofile.techdemo.frontend;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("rest")
public class FrontendApplication extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();
        classes.add(PropertiesResource.class);
        classes.add(TolerantResource.class);
        return classes;
    }
}
