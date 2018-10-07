package ch.ocram.microprofile.techdemo.backend;

import org.eclipse.microprofile.health.Health;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;

import javax.enterprise.context.ApplicationScoped;

@Health
@ApplicationScoped
public class HealthProbe implements HealthCheck {

    private int countdown;

    public void setCountdown(int countdown) {
        this.countdown = countdown;
    }

    @Override
    public HealthCheckResponse call() {
        HealthCheckResponse healthCheckResponse = HealthCheckResponse
                .named("probe")
                .state(countdown <= 0)
                .withData("countdown", countdown)
                .build();

        if (countdown > 0) {
            countdown--;
        }
        return healthCheckResponse;
    }
}
