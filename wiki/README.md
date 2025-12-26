# exception-core-lib

exception-core-lib — A production-ready exception handling library for Spring Boot 3.
This library solves the following problems in a centralized and standardized way:

* BaseException and ErrorCode architecture
* Locale-based (i18n) error messages
* Custom JSON error response
* MDC + TraceId (request tracing)
* Automatic exception logging with AOP

### Spring Boot AutoConfiguration (plug & play)
After adding the Library to the project, it works without writing additional configuration.

## Features

* All business exceptions via BaseException
* ErrorCode interface + ability to write your own enums
* i18n with messages_az.properties, messages_en.properties etc.
* X-Trace-Id header + MDC integration
* Automatic logging of all exceptions with AOP
* Unified and stable error response format
* Spring Boot 3 AutoConfiguration support

## Architecture
```psql
exception-core-lib
 ├── exception
 │   ├── BaseException
 │   ├── BusinessException
 │   ├── NotFoundException
 │   ├── ValidationException
 │   └── ErrorCode
 │
 ├── config
 │   ├── ExceptionProperties
 │   └── ExceptionMessageResolver
 │
 ├── web
 │   ├── GlobalExceptionHandler
 │   ├── TraceIdFilter
 │   └── ErrorResponse
 │
 ├── log
 │   └── ExceptionLoggingAspect
 │
 └── autoconfig
     └── ExceptionAutoConfiguration
```

## Requirements
* **Java 17+**
* **Spring Boot 3.x**
* **Gradle or Maven**

## Installation
#### 1.Add Library
```gradle
repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    implementation "com.imran:exception-core-lib:1.0.0"
}
```


#### Locale-based Error Messages (i18n)

The library reads the message source from the user project.

_`application.yml`_
```yaml
imran:
  exception:
    messages-basename: i18n/errors
    trace-id-header-name: X-Trace-Id
    trace-id-mdc-key: traceId
    log-stack-trace: true
```


#### Message files

**_`src/main/resources/i18n/errors_az.properties`_**

```properties
errors.internal=Daxili sistem xətası baş verdi
errors.validation=Validasiya xətası
user.not_found=İstifadəçi tapılmadı
```


_`src/main/resources/i18n/errors_en.properties`_

```properties
errors.internal=Internal system error
errors.validation=Validation error
user.not_found=User not found
```

#### ErrorCode design

ErrorCode interface

```java
public interface ErrorCode {
    String getCode();
    HttpStatus getHttpStatus();
    String getMessageKey();
}
```

Custom ErrorCode enum (user-side)

```java
public enum UserErrorCode implements ErrorCode {

    USER_NOT_FOUND(
        "USER_NOT_FOUND",
        HttpStatus.NOT_FOUND,
        "user.not_found"
    );

    private final String code;
    private final HttpStatus status;
    private final String messageKey;

    UserErrorCode(String code, HttpStatus status, String messageKey) {
        this.code = code;
        this.status = status;
        this.messageKey = messageKey;
    }

    public String getCode() { return code; }
    public HttpStatus getHttpStatus() { return status; }
    public String getMessageKey() { return messageKey; }
}
```

#### Exception usage
```java
@GetMapping("/users/{id}")
public UserDto getUser(@PathVariable Long id) {
    throw new NotFoundException(UserErrorCode.USER_NOT_FOUND);
}
```

#### Response Format (standart)
```json
{
    "code": "USER_NOT_FOUND",
    "message": "User not found",
    "traceId": "a2c3f91e-9c63-4d71-bef7-9f51bcb1b912",
    "path": "/users/10",
    "status": 404,
    "timestamp": "2025-01-10T12:34:56.123Z",
    "details": {}
}
```

## TraceId and MDC
Automatically generated traceId for each request

X-Trace-Id header is added to response

Stored within MDC

Logback example
```xml
<pattern>
    %d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - [%X{traceId}] %msg%n
</pattern>
```

## Exception Logging (AOP)

Library automatically:

* @RestController
* @Controller
* @Service

It logs all exceptions thrown in it.

BaseException → as business error

Others → as unexpected error

Stacktrace behavior is controlled by the log-stack-trace property.

## AutoConfiguration

The library uses Spring Boot AutoConfiguration.

Activation file:

`META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports
`

Therefore:

* @Enable...
* @ComponentScan

no need to write.

### Useful config for debugging
```yaml
logging:
  level:
    org.springframework.boot.autoconfigure: DEBUG
```

### Publishing

To push the library to your local Maven repo:

`./gradlew clean publishToMavenLocal`


Then use it in your consumer project.

### Why this library?
* No need to write exception handling in every project
* Single error contract in Microservices
* Log and trace analysis is much easier
* i18n + observability ready

### Future plans (roadmap)

* Spring WebFlux support
* OpenAPI / Swagger error schema automatic export
* Central registry for ErrorCodes
* Kafka / RabbitMQ trace propagation



# **Imran Rasulzade**
### _Backend Engineer · Spring Boot · Microservices_