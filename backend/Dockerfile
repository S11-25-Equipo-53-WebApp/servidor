# Etapa 1: Copila la aplicacion
FROM maven:3.9.7-eclipse-temurin-21-alpine AS build

# Define la carpeta de trabajo dentro del container
WORKDIR /app

# Copia el archivo pom.xml y los archivos de dependencias para la carpeta de trabajo
COPY pom.xml ./
COPY src ./src

# Copila la aplicacion
RUN mvn package

# Etapa 2: Criar la imagem final para ejecutarla usando el JRE para que sea mas leve
FROM eclipse-temurin:21-jre-alpine

# Define perfil a ser utilizado
ENV PROFILE=prod

# Define la carpeta de trabajo dentro del container
WORKDIR /app

# Cria un argumento para el nombre de la aplicacion
ARG JAR_FILE=target/*.jar

# Copia el jar compilado de la etapa anterior
COPY --from=build /app/${JAR_FILE} app.jar

# Expone la puerta de la aplicacion
EXPOSE 8080

# Define el comando default para iniciar la aplicacion
CMD ["java", "-jar", "app.jar"]

#Para rodar la aplicacion de forma local en linea de comando
#docker build -t backend:latest .
#docker run -p 8080:8080 backend:latest