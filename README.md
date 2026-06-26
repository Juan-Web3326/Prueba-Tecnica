# Documentación Técnica - API Bancaria

Este documento detalla la arquitectura, patrones de diseño y principios aplicados en el desarrollo de la API Bancaria para la prueba técnica.

## 1. Tipo de Arquitectura Utilizada

El proyecto está construido utilizando una **Arquitectura en Capas (Layered Architecture)**, la cual es el estándar en aplicaciones Spring Boot. Esta arquitectura divide la aplicación en componentes con responsabilidades únicas y bien definidas, facilitando el mantenimiento, las pruebas (testing) y la escalabilidad.

Las capas utilizadas son:

1.  **Capa de Presentación / Controladores (`controller`)**: 
    - Contiene los controladores REST (`ClienteController`, `CuentaController`, `TransaccionController`).
    - **Responsabilidad:** Exponer los endpoints HTTP (GET, POST, etc.), recibir las peticiones JSON (Postman/Frontend), enviarlas a la capa de servicio y devolver las respuestas HTTP con el código de estado correcto (200 OK, 201 Created, 400 Bad Request).
2.  **Capa de Negocio / Servicios (`service`)**:
    - Contiene las interfaces y sus implementaciones (`ClienteServiceImpl`, etc.).
    - **Responsabilidad:** Contener toda la lógica central del negocio y validaciones (e.g., validar que un cliente sea mayor de edad, verificar saldo suficiente para un retiro). Es el "cerebro" de la aplicación.
3.  **Capa de Acceso a Datos / Repositorios (`repository`)**:
    - Contiene las interfaces que heredan de Spring Data JPA (`JpaRepository`).
    - **Responsabilidad:** Comunicarse directamente con la base de datos MySQL (hacer los INSERT, SELECT, UPDATE, DELETE) sin tener que escribir código SQL manual, abstrayendo la complejidad de la base de datos.
4.  **Capa de Modelo / Entidades (`entity`)**:
    - Contiene las clases Java anotadas con `@Entity` (`Cliente`, `Cuenta`, `Transaccion`).
    - **Responsabilidad:** Representar (mapear) exactamente la estructura de las tablas en la base de datos relacional.

El flujo de información es siempre unidireccional y estricto: **Cliente/Postman -> Controller -> Service -> Repository -> Base de Datos**.

## 2. Patrones de Diseño Aplicados

A lo largo del proyecto, gracias al ecosistema de Spring y Java, se han aplicado los siguientes patrones de diseño:

- **Repository Pattern (Patrón Repositorio):** Aísla la lógica de acceso a datos del resto de la aplicación. En lugar de interactuar directamente con la base de datos, los servicios interactúan con la interfaz del repositorio (`ClienteRepository`), el cual se encarga de las operaciones.
- **Dependency Injection / Inversion of Control (Inyección de Dependencias):** Utilizado a través del contenedor de Spring (IoC). En lugar de que los Controladores "instancien" o creen manualmente los Servicios con `new`, Spring Boot se los "inyecta" por constructor. Esto desacopla las clases.
- **Singleton:** Por defecto, todos los Beans de Spring (como los componentes de Service y Controller) son Singletons, lo que significa que solo existe una única instancia de cada clase en toda la memoria de la aplicación manejando miles de peticiones, ahorrando recursos.
- **Proxy Pattern:** Spring usa proxys internos cada vez que usamos la anotación `@Transactional`. El proxy intercepta la llamada al método, abre la transacción de base de datos, ejecuta el código y luego hace el *commit* o *rollback* automáticamente.

## 3. Principios SOLID

El código respeta los pilares de diseño de software (SOLID):

- **S - Single Responsibility (Responsabilidad Única):** Cada clase tiene un único trabajo. El `ClienteController` solo maneja peticiones web, el `ClienteServiceImpl` solo maneja lógica bancaria y el `ClienteRepository` solo guarda datos.
- **O - Open/Closed (Abierto/Cerrado):** El uso de interfaces en la capa de servicios (ej. `ClienteService`) permite que si mañana el banco necesita otra implementación del servicio (ej. `ClienteServiceVIPImpl`), se puede extender sin modificar el código que ya funciona.
- **D - Dependency Inversion (Inversión de Dependencia):** Los módulos de alto nivel (como los `Controllers`) no dependen de implementaciones concretas de bajo nivel (`ClienteServiceImpl`), sino de abstracciones (la interfaz `ClienteService`). Esto permite cambiar la implementación interna sin romper el controlador.

## 4. Principio ACID en Base de Datos

Las transacciones bancarias (especialmente la transferencia) deben ser completamente seguras. Para garantizar la integridad de los datos, se usó el principio **ACID** apoyado por la anotación `@Transactional` en Spring Boot:

- **Atomicity (Atomicidad):** La transferencia requiere descontar dinero de una cuenta y sumarlo a otra. Con `@Transactional`, ambas operaciones se agrupan. Si la segunda operación falla (ej. se va la luz o el servidor crashea), la primera se deshace automáticamente (*rollback*). O se hace todo, o no se hace nada. Nunca se pierde dinero.
- **Consistency (Consistencia):** La base de datos pasa de un estado válido a otro estado válido. Reglas como `saldo >= 0` en cuentas de ahorros previenen estados imposibles en el sistema.
- **Isolation (Aislamiento):** Si dos personas intentan transferir dinero a la misma cuenta exactamente en el mismo milisegundo, la base de datos aisla las transacciones para que no se sobreescriban los saldos y se sumen correctamente de manera secuencial.
- **Durability (Durabilidad):** Una vez que el método `transferir` termina sin errores, Spring hace el *commit*. A partir de ese milisegundo, los datos están guardados físicamente en el disco duro por MySQL, y sobrevivirán incluso si el servidor se apaga inmediatamente después.

## 5. Pruebas de Componentes (Testing)

Se implementaron pruebas unitarias automatizadas utilizando **JUnit 5** y **Mockito** para garantizar la calidad del software.
- Se aislaron los servicios (`ClienteServiceImpl`, `TransaccionServiceImpl`) simulando el comportamiento de la base de datos con *Mocks* (`@Mock`).
- Se probaron escenarios de éxito (ej. transferencia exitosa) y casos límite/errores (ej. cliente menor de edad, saldo insuficiente en cuenta de ahorros, transferencia a la misma cuenta).

## 6. Despliegue Dockerizado

El proyecto está completamente dockerizado para facilitar su ejecución en cualquier entorno sin depender de instalaciones locales de Java o MySQL.
- **Dockerfile:** Construcción en múltiples etapas (Multi-stage build). Usa Maven para compilar el código y una imagen ligera de Java 21 (`eclipse-temurin:21-jre`) para ejecutar el `.jar` resultante.
- **Docker Compose:** Orquesta dos servicios (`banco_app` y `banco_db`). Configura la persistencia de datos mediante volúmenes y las credenciales mediante variables de entorno.

**Instrucciones de ejecución:**
```bash
docker-compose up --build
```

## 7. Scripts DDL y DML

El proyecto incluye scripts SQL para la creación y población de la base de datos:
- **DDL (Data Definition Language):** Definición de la estructura de las tablas (`clientes`, `cuentas`, `transacciones`) y sus relaciones. *(Implementado a través de Spring JPA Hibernate `ddl-auto`)*.
- **DML (Data Manipulation Language):** Datos semilla (inserts) para tener cuentas listas al iniciar el proyecto.

## 8. Servicios Cloud y Control de Versiones

- **Servicios Cloud:** Para esta entrega no se utilizaron servicios en la nube (AWS, Azure, GCP). El despliegue es 100% contenerizado en local, reduciendo dependencias externas.
- **Control de Versiones:** Se utilizó **Git** para mantener el historial del proyecto.
