# Compile
FROM eclipse-temurin:17-jdk as compiler
COPY . .
RUN chmod +x ./gradlew
RUN ./gradlew bootJar

# Build & Creating Layered Jars
FROM eclipse-temurin:17-jdk as builder
COPY --from=compiler build/libs/*.jar ./app.jar
RUN java -Djarmode=layertools -jar app.jar extract

# Creating the Docker Image
FROM eclipse-temurin:17-jdk

RUN useradd -m backend
USER backend

WORKDIR /backend

COPY --chown=backend --from=builder dependencies/ ./
COPY --chown=backend --from=builder snapshot-dependencies/ ./
COPY --chown=backend --from=builder spring-boot-loader/ ./
COPY --chown=backend --from=builder application/ ./

RUN chmod -R 755 /backend

EXPOSE 8080
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]