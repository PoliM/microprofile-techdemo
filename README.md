# Techdemo for Microprofile stuff
What is shown:


## OpenAPI
Both, frontend and backend services, have the OpenAPI available under `/openapi`. There is also a UI under `/openapi/ui`.

### Frontend
Uses a contract first approach and generates the REST interfaces from a given openapi.yaml file. Then it uses the interfaces to implement the REST service.

### Backend
Defines JAX-RS REST services and enriches them with OpenAPI annotations.


## Config
For example, the backend URL is configured in the frontend service in the class `TolerantResource`.


## Health
The backend service has a `HealthProbe` that can be configured via REST call to fail a couple of times. Can be queried under `/health`.


## Metrics
The REST services are @Timed and there is a @Counted on the health probe. Can be queried under `/metrics`.


## REST client
The frontend uses the Microprofile REST-Client API to call the backend. This also has a "special" config property for the backend URL.


## Fault Tolerance

The fault tolerant stuff is also visible in the metrics.

### Demonstration 1: Retry
The `tolerantRetry` operation fails 80% of the time. With a `@Retry` you can show that it gets called multiple times (yes, there is an ugly hack to count the calls).
To configure the retry behaviour you can change the value `.....tolerantRetry/Retry/maxRetries=100` in the `microprofile-config.properties` file. 

### Demonstration 2: Timeout / Fallback
The `tolerantTimeout` operation takes a parameter that tells the backend how long to stall. If you make that larger than 1000ms then the frontent will timeout and use 
the fallback method.

### Demonstration 3: CircuitBreaker / Fallback
This one is a bit more complicated. The `tolerantCircuitBreaker` method takes a parameter that tells it to fail or not. You should be able to observe the following behaviours:

* Calling it with `false` will return status code 200 and an increasing counter
* Hitting it with `true` will only count up three more times. The status code is 503 (service unavailable). 
* Waiting for 10 seconds will bring the circuit into its "half closed" state. Hitting it with `true` will count up one time (because it fails and will open the circuit again). 
* Switching back to `false` should close the circuit after two callls and after 10 second. (it happens earlier, but I don't know why yet)


## Future stuff
* Tracing
* JWT
* gRPC / RSocket?
* Comparison to https://12factor.net/ and "Beyond The 12 Factor App"

## OpenLiberty Bugs?
* JSON Metrics ignore TimeUnit
* Fallback does not work with private fallback method even though the example in the specs is made that way


## Development

### Project creation
```bash
mvn archetype:generate \
    -DarchetypeGroupId=net.wasdev.wlp.maven \
    -DarchetypeArtifactId=liberty-archetype-webapp \
    -DarchetypeVersion=2.6.1 \
    -DlibertyPluginVersion=2.6.1 \
    -DruntimeVersion=18.0.0.3 \
    -DgroupId=ch.ocram.microprofile.techdemo \
    -DartifactId=frontend \
    -Dversion=1.0.0-SNAPSHOT
```

### Start a Swagger editor
```bash
docker run -d -p 7510:8080 swaggerapi/swagger-editor
```
