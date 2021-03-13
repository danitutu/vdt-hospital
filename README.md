## vdt-hospital

A hospital app which can be used as a starter kit for various projects or it can be a good starting point for those who would like to learn more about enterprise application development and technology.

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

Everyone is welcome to contribute. There are some rules though:
- maintain code quality
- write tests 
- use best practices
- keep things simple and do not over-engineer
- document the APIs