# Documentación Técnica - API Bancaria

Este documento detalla la arquitectura, patrones de diseño y principios aplicados en el desarrollo de la API Bancaria para la prueba técnica.

## 1. Tipo de Arquitectura Utilizada

El proyecto fue migrado y estructurado bajo la **Arquitectura Hexagonal (Puertos y Adaptadores)**. Esta arquitectura desacopla totalmente la lógica de negocio (Dominio) de frameworks externos (Spring Boot, JPA, MySQL, REST).

Las capas utilizadas son:

1. **Dominio (`com.prueba_tecnica.domain`)**:
   - **Modelos (`model`):** Clases puras de Java (`Cliente`, `Cuenta`, `Transaccion`) sin anotaciones de Spring ni JPA.
   - **Puertos de Entrada (`ports.in`):** Interfaces de Casos de Uso (`ClienteUseCase`, `CuentaUseCase`, `TransaccionUseCase`).
   - **Puertos de Salida (`ports.out`):** Interfaces de Repositorio (`ClienteRepositoryPort`, etc.).
2. **Aplicación (`com.prueba_tecnica.application`)**:
   - **Servicios (`service`):** Implementaciones puras de los casos de uso que coordinan la lógica de negocio consumiendo los puertos de salida.
3. **Infraestructura (`com.prueba_tecnica.infrastructure`)**:
   - **Adaptadores de Salida (`adapters.out.persistence`):** Mappers, Entidades JPA (`ClienteJpaEntity`) y Repositorios Spring Data JPA que implementan los puertos de salida.
   - **Adaptadores de Entrada (`adapters.in.web`):** Controladores REST que exponen los endpoints consumiendo los puertos de entrada.
   - **Configuración (`config`):** `ApplicationConfig` para registrar los Beans e inyectar dependencias.

## 2. Patrones de Diseño Aplicados

- **Ports & Adapters (Hexagonal):** Desacoplamiento total del núcleo de negocio.
- **Repository Pattern:** Aísla el acceso a datos detrás de los puertos de salida (`ClienteRepositoryPort`).
- **Mapper Pattern:** Conversión bidireccional entre objetos del Dominio y Entidades JPA.
- **Dependency Injection / IoC:** Configurado explícitamente mediante `@Bean` en la capa de infraestructura.

## 3. Principios SOLID

- **S - Responsabilidad Única:** Separación estricta entre modelos puros, persistencia y exposición web.
- **O - Abierto/Cerrado & D - Inversión de Dependencia:** Las capas internas dependen únicamente de abstracciones (interfaces de puertos), permitiendo cambiar de base de datos o tecnología sin tocar el dominio.

## 4. Principio ACID en Base de Datos

Las transacciones bancarias mantienen las garantías de **Atomicidad, Consistencia, Aislamiento y Durabilidad** en MySQL mediante el manejo de persitencia y mapeo consistente en la capa de infraestructura.

## 5. Despliegue Dockerizado

```bash
docker-compose up --build
```

## 6. Scripts DDL y DML

Ubicados en la carpeta `sql/`:
- `ddl.sql`: Definición de tablas relacionales.
- `dml.sql`: Datos semilla iniciales.
