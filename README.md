# Prueba Técnica AIT

Este ecosistema de microservicios está desarrollado con **Java** y **Spring Boot**, diseñado para la gestión de conductores y órdenes de transporte.

## Arquitectura del Sistema
* **Eureka Server:** Servidor de descubrimiento (Puerto: 8761).
* **API Gateway:** Punto de entrada único (Puerto: 8090).
* **Driver MSV:** Gestión de conductores y disponibilidad (Puerto: 8081).
* **Order MSV:** Gestión de órdenes de transporte (Puerto: 8082).
* **Commons DTO:** Librería compartida para modelos y excepciones.

## Tecnologías Utilizadas
* **Backend:** Java 17 & Spring Boot 3.5.x
* **Documentación** Spring Doc Swagger http://localhost:8081/swagger-ui/index.html#/  y  http://localhost:8082/swagger-ui/index.html#/
* **Postman** Pruebas de cada método en Postman https://www.postman.com/carlosworkspace/workspace/prueba-ait
* **Contenerización:** Docker & Docker Compose
* **Base de Datos:** Oracle Database (DRIVER y ORDERS)
* **Comunicación:** OpenFeign & Eureka Discovery Service

## Guía de Despliegue

### 1. Compilación de artefactos
Desde la raíz del proyecto, ejecutar la limpieza e instalación de la librería común y el empaquetado de servicios:
```bash
# Instalar commons primero
cd commons-dto && ./mvnw clean install -DskipTests && cd ..

# Empaquetar el resto de servicios
./mvnw clean package -DskipTests

# Una vez hecho esto para desplgar en docker hacer este comando
docker-compose up -d --build
