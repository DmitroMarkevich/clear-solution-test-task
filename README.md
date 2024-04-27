## **How to run in IDEA**

### APPLICATION CONFIG

Create environment variables:

```
${ACCESS_EXPIRATION_TIME} -> Access token expirationn time.
${REFRESH_EXPIRATION_TIME} -> Refresh token expirationn time.
```

Example for Windows, run CMD and execute commands:

```
setx DB_USERNAME "user"
setx DB_PASSWORD "12345"
```

Example for Linux, run SH and execute commands:

```
export DB_USERNAME='user'
export DB_PASSWORD='12345'
```

For connection use defaults:

```
DB_USERNAME = user
DB_PASSWORD = 12345
```

## **Technologies**

- Java 17
- Spring Boot 3
- Maven
- H2 (in-memory)
- Flyway
- Hibernate
