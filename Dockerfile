FROM eclipse-temurin:17-jdk-focal as build
WORKDIR /app

# Copy maven files first (for better layer caching)
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Make the mvnw script executable
RUN chmod +x ./mvnw

# Download dependencies (this layer can be cached if pom.xml doesn't change)
RUN ./mvnw dependency:go-offline -B

# Copy source code
COPY src src

# Build the application
RUN ./mvnw package -DskipTests
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

# Runtime stage
FROM eclipse-temurin:17-jre-focal
VOLUME /tmp
ARG DEPENDENCY=/app/target/dependency

# Copy the dependency application content from build stage
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app

# Set the entry point
ENTRYPOINT ["java","-cp","app:app/lib/*","com.example.apimanager.ApiManagerApplication"]