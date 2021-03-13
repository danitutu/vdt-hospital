A hospital app which can be used as a starter kit for various projects or it can be a good starting point for those who would like to learn more about enterprise application development and technology.

### Keycloak URLs

```
KEYCLOAK_URL=https://.../auth
Keycloak:                 $KEYCLOAK_URL
Keycloak Admin Console:   $KEYCLOAK_URL/admin
Keycloak Account Console: $KEYCLOAK_URL/realms/myrealm/account
```

#### Test app
https://www.keycloak.org/app

### Development

Docker and Kubernetes need to be installed in your system. For Kubernetes you can use `Docker for Windows` or `minikube`. Then you can make use of `start-all-k8s-services.sh` and `remove-all-k8s-services.sh` in order to put the application in place.

When developing, you can start the involved services or you can configure to connect to the existing ones. 

### Contribute

Every idea may have a place here. Yours too. Everyone is welcome to contribute to the project but there are some things that should be kept in mind:
- maintain code quality
- write tests 
- use best practices
- keep things simple and do not over-engineer
- document the APIs

#### How it works?
There will always be a pull request in order to get your code to the main branch and the pull request will require my approval. It may be a good idea to open an issue about what you are going to do and how.

There may be open issues which contain requirements for wanted feature, just have a look. It may be a good starting point for your contribution. 