# Cooperator - DP27

## Backend

The backend uses Maven to run tests and run the application, so you will need to [install Maven](https://maven.apache.org/install.html).

To run the backend: 
```bash
cd Backend
mvn spring-boot:run
```

To run service tests (from the `Backend` folder):
```bash
mvn test
```

To run integration and Cucumber tests:
```bash
mvn failsafe:integration-test
```

To format all Java files:
```bash
mvn com.coveo:fmt-maven-plugin:format
```

## Frontend

The frontend uses Vue and [Quasar](https://quasar.dev/), which is a Vue framework. You will need to install the [Quasar CLI](https://quasar.dev/quasar-cli/installation) to run the frontend, and it needs to be at least version 1.9.13. To upgrade the Quasar CLI, follow [these instructions](https://quasar.dev/start/upgrade-guide). 

To run the frontend:
```bash
quasar dev
```

## Authentication

The application uses the McGill LDAP system for authentication.
