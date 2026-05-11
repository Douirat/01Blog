# Spring Boot: `@SpringBootApplication` and the DI Lifecycle

---

## 1. What `@SpringBootApplication` Actually Is

`@SpringBootApplication` is a convenience annotation that combines three core annotations:

```java
@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan
```

Each one activates a distinct mechanism in the Spring lifecycle.

---

## 2. How It Triggers the DI Lifecycle

When you write:

```java
@SpringBootApplication
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
```

Spring Boot executes the following phases:

---

### Phase 0 — Bootstrapping *(before DI even starts)*

`SpringApplication.run()`:

- Starts JVM-level setup -> https://www.geeksforgeeks.org/java/how-jvm-works-jvm-architecture/
- Creates the `ApplicationContext`
- Prepares the environment (config, profiles, properties)

---

### Phase 1 — Component Scanning (`@ComponentScan`)

This is where annotations start to matter.

Spring scans the package of `App` and all subpackages, looking for:

- `@Component`
- `@Service`
- `@Repository`
- `@Controller`

**Example:**

```java
@Service
class EmailService {}
```

This class becomes "known" to Spring.

> **Without this phase → Spring sees nothing → no DI possible.**

---

### Phase 2 — Auto-Configuration (`@EnableAutoConfiguration`)

This is Spring Boot's magic layer.

It detects libraries on the classpath and creates default beans automatically:

| Library present on classpath | Bean(s) auto-configured |
|------------------------------|------------------------|
| `spring-web`                 | Embedded Tomcat        |
| `spring-data-jpa`            | `EntityManager`        |
| `jackson`                    | JSON converter         |

> You don't need to manually define infrastructure beans.

---

### Phase 3 — Bean Creation + Dependency Injection

Now Spring has a full "map" of beans. It:

1. Instantiates beans
2. Resolves dependencies
3. Injects them (constructor injection preferred)

**Example:**

```java
@Service
class OrderService {
    private final NotificationService service;

    public OrderService(NotificationService service) {
        this.service = service;
    }
}
```

Spring's resolution steps:

1. Finds `OrderService`
2. Sees it needs a `NotificationService`
3. Finds the implementation (`EmailService`)
4. Builds the complete object graph

---

## Summary

```
@SpringBootApplication
        │
        ├── @SpringBootConfiguration   →  marks this as a config class
        ├── @ComponentScan             →  Phase 1: discovers beans
        └── @EnableAutoConfiguration   →  Phase 2: wires infrastructure

SpringApplication.run()
        │
        ├── Phase 0: Bootstrap (env, context)
        ├── Phase 1: Scan & register bean definitions
        ├── Phase 2: Auto-configure infrastructure beans
        └── Phase 3: Instantiate + inject dependencies
```