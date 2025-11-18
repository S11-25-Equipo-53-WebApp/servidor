# Backend - Startup CRM

Descripción del Proyecto

🧩 Startup CRM
Este proyecto es un CRM inteligente en desarrollo, orientado a startups que necesitan gestionar leads y clientes de manera simple, colaborativa e integrada. El sistema centralizará conversaciones, automatizará seguimientos y ofrecerá métricas accesibles, con integración nativa a WhatsApp y correo electrónico.
Sector de Negocio
Cross-Industry
# Necesidad del Cliente
El cliente busca un CRM inteligente con:
- Integración nativa a WhatsApp y correo electrónico.
- Gestión de conversaciones en tiempo real.
- Automatización de seguimientos.
- Segmentación de usuarios (por ejemplo, leads activos y clientes en seguimiento).
- Paneles de métricas simples y colaborativos.
- Interacción asincrónica y personalización.
# Objetivo
Desarrollar un CRM inteligente para startups, con:
- Centralización de conversaciones.
- Automatización de tareas y seguimientos.
- Segmentación de usuarios.
- Experiencia simple y colaborativa.
# Requerimientos Funcionales
- Gestión de contactos y segmentación por estado del funnel.
- Integración de canales de comunicación.
- Envío y registro de emails con etiquetas y plantillas.
- Recordatorios automáticos para tareas y seguimientos.
- Panel de métricas y analítica.
- Exportación de datos en CSV o PDF.
- Configuración de etiquetas, vistas y filtros guardados.
# Integraciones Externas
- WhatsApp Cloud API (Meta).
- API SMTP o Brevo para email.
# Entregables Esperados
- Prototipo funcional con flujos básicos de usuario, comunicación y segmentación.
- Panel de métricas con visualización de KPIs clave (contactos activos, mensajes enviados, tasa de respuesta).
- Documentación de endpoints y guía de instalación.
# Tecnologías Utilizadas
- Java 21
- Maven 3.9.7
- Spring Boot (dependencias: Spring Web, DevTools, Lombok, JPA)
- PostgreSQL 16
- Driver PostgreSQL
- Eclipse (IDE)
- DBeaver (Base de datos)
- Docker (multi-stage build)
- Render (deploy)
# Dockerfile
# Etapa 1: Compila la aplicación
```bash
FROM maven:3.9.7-eclipse-temurin-21-alpine AS build
WORKDIR /app
COPY pom.xml ./
COPY src ./src
RUN mvn package
```

# Etapa 2: Crea la imagen final
```bash
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
ARG JAR_FILE=target/*.jar
COPY --from=build /app/${JAR_FILE} app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
Cómo Ejecutar Localmente
```
# Construir la imagen:

```bash
docker build -t backend:latest .
```

Ejecutar el contenedor:

```bash
docker run -p 8080:8080 backend:latest
```

La aplicación estará disponible en:
http://localhost:8080
# Despliegue
El despliegue se realizó en Render utilizando la imagen Docker generada. Para más detalles sobre configuración en Render, consulte: https://render.com/docs
# Próximos Pasos
- Implementar integración con WhatsApp Cloud API.
- Configurar envío de correos vía SMTP/Brevo.
- Crear panel de métricas con KPIs.
- Añadir pruebas automatizadas.
- Configurar CI/CD para integración continua.
# Estado del Proyecto
🚧 En desarrollo. Solo se ha creado la estructura inicial.
