# exception-core-lib

exception-core-lib — Spring Boot 3 üçün hazırlanmış, istehsal səviyyəli (production-ready) exception handling kitabxanasıdır.
Bu library aşağıdakı problemləri mərkəzləşdirilmiş və standart şəkildə həll edir:

* BaseException və ErrorCode arxitekturası
* Locale-based (i18n) error message-lər
* Custom JSON error response
* MDC + TraceId (request tracing)
* AOP ilə avtomatik exception logging

### Spring Boot AutoConfiguration (plug & play)
Library-ni layihəyə əlavə etdikdən sonra əlavə konfiqurasiya yazmadan işləyir.

## Xüsusiyyətlər

*  BaseException üzərindən bütün business exception-lar
*  ErrorCode interface + öz enum-larını yazmaq imkanı
*  messages_az.properties, messages_en.properties və s. ilə i18n
*  X-Trace-Id header + MDC inteqrasiyası
*  AOP ilə bütün exception-ların avtomatik loglanması
*  Vahid və stabil error response formatı
*  Spring Boot 3 AutoConfiguration dəstəyi

## Arxitektura
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

## Tələblər
* **Java 17+**
* **Spring Boot 3.x**
* **Gradle və ya Maven**

## Quraşdırma
#### 1.Library-ni əlavə et
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

Library message source-u istifadəçi layihəsindən oxuyur.

_`application.yml`_
```yaml
imran:
  exception:
    messages-basename: i18n/errors
    trace-id-header-name: X-Trace-Id
    trace-id-mdc-key: traceId
    log-stack-trace: true
```


#### Mesaj faylları

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

#### ErrorCode dizaynı

ErrorCode interface

```java
public interface ErrorCode {
    String getCode();
    HttpStatus getHttpStatus();
    String getMessageKey();
}
```

Custom ErrorCode enum (istifadəçi tərəfində)

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

#### Exception istifadəsi
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
    "message": "İstifadəçi tapılmadı",
    "traceId": "a2c3f91e-9c63-4d71-bef7-9f51bcb1b912",
    "path": "/users/10",
    "status": 404,
    "timestamp": "2025-01-10T12:34:56.123Z",
    "details": {}
}
```

## TraceId və MDC
Hər request üçün avtomatik traceId yaradılır

X-Trace-Id header response-a əlavə olunur

MDC daxilində saxlanılır

Logback nümunəsi
```xml
<pattern>
    %d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - [%X{traceId}] %msg%n
</pattern>
```

## Exception Logging (AOP)

Library avtomatik olaraq:

* @RestController
* @Controller
* @Service

içində atılan bütün exception-ları loglayır.

BaseException → business error kimi

Digərləri → unexpected error kimi

Stacktrace davranışı log-stack-trace property-si ilə idarə olunur.

## AutoConfiguration

Library Spring Boot AutoConfiguration istifadə edir.

Aktivləşmə faylı:

`META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports
`

Bu səbəbdən:

* @Enable...
* @ComponentScan

yazmağa ehtiyac yoxdur.

### Debug üçün faydalı config
```yaml
logging:
  level:
    org.springframework.boot.autoconfigure: DEBUG
```

### Publishing

Library-ni local Maven repo-ya göndərmək üçün:

`./gradlew clean publishToMavenLocal`


Sonra consumer layihədə istifadə et.

### Niyə bu library?
* Hər layihədə exception handling yazmaq məcburiyyəti yoxdur
* Microservice-lərdə vahid error contract
* Log və trace analiz çox asanlaşır
* i18n + observability hazır gəlir

### Gələcək planlar (roadmap)

* Spring WebFlux dəstəyi
* OpenAPI / Swagger error schema avtomatik export
* ErrorCode-lar üçün central registry
* Kafka / RabbitMQ trace propagation



# **Imran Rasulzade**
### _Backend Engineer · Spring Boot · Microservices_