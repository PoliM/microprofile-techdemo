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
TBD


## Future stuff
* Tracing
* JWT
* gRPC
* Comparison to https://12factor.net/ and "Beyond The 12 Factor App"


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
