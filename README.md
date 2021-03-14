## vdt-hospital

A hospital app which can be used as a starter kit for various projects.

### What can you find here?

- Kotlin, Spring Framework, Spring Boot, Spring Security, Spring Webflux, 
  Springdoc, Zookeeper, Kafka, Mongo, mongo-express, OAuth2, OpenID Connect,
  Postgres, Keycloak, Elasticsearch, Logstash, Kibana, Docker, Mockk
- Webflux functional endpoints
- Kotlin coroutines usage
- documenting functional endpoints for `coRouter` using `springdoc-openapi`
- unit tests using `Mockk`
- log aggregation using ELK stack
- configure resource server using `spring-security-oauth2-resource-server` and `spring-security-oauth2-jose`
- microservice architecture
- software development patterns implementation

### Development

1. Start containers using `./docker/docker-compose.yml`
2. Start services you would like to run

#### Keycloak URLs

```
KEYCLOAK_URL=https://.../auth
Keycloak:                 $KEYCLOAK_URL
Keycloak Admin Console:   $KEYCLOAK_URL/admin
Keycloak Account Console: $KEYCLOAK_URL/realms/myrealm/account
```

##### Test app
https://www.keycloak.org/app

### Contribute

Everyone is welcome to contribute. 
