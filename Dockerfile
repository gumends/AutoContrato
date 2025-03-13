# Fase de build
FROM maven:3.9-eclipse-temurin-23 AS build

# Define o diretório de trabalho
WORKDIR /app

# Copia os arquivos necessários para o build
COPY pom.xml ./
COPY src ./src

# Compila o projeto e gera o JAR
RUN mvn clean package -DskipTests

# Fase final
FROM openjdk:23-oracle

# Define o diretório de trabalho
WORKDIR /app

# Copia o JAR gerado corretamente
COPY --from=build /app/target/*.jar /app/app.jar

# Expõe a porta 8080
EXPOSE 8080

# Comando de entrada
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
