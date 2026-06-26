# ── ETAPA 1: Compilar la aplicación ────────────
# Usamos una imagen con Maven y Java 21 para compilar el proyecto
FROM maven:3.9.6-eclipse-temurin-21 AS build

# Establecemos la carpeta de trabajo dentro del contenedor
WORKDIR /app

# Copiamos primero el pom.xml para aprovechar el caché de dependencias de Docker
COPY pom.xml .

# Descargamos todas las dependencias (esto se cachea si el pom.xml no cambia)
RUN mvn dependency:go-offline -B

# Ahora copiamos el código fuente
COPY src ./src

# Compilamos y empaquetamos en un .jar (sin correr los tests)
RUN mvn clean package -Dmaven.test.skip=true

# Forzar UTF-8 en Maven dentro del contenedor Linux
ENV MAVEN_OPTS="-Dfile.encoding=UTF-8"

# Compilamos y empaquetamos en un .jar (sin correr los tests)
RUN mvn clean package -Dmaven.test.skip=true

# ── ETAPA 2: Imagen final liviana ────────────────
# Solo usamos Java 21 para correr, sin Maven (imagen más pequeña)
FROM eclipse-temurin:21-jre

# Carpeta de trabajo en el contenedor final
WORKDIR /app

# Copiamos SOLO el .jar generado en la etapa anterior
COPY --from=build /app/target/*.jar app.jar

# Puerto que expone la aplicación Spring Boot
EXPOSE 8080

# Comando que se ejecuta al iniciar el contenedor
ENTRYPOINT ["java", "-jar", "app.jar"]