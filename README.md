# Prueba Técnica AIT

Este ecosistema de microservicios está desarrollado con **Java 17** y **Spring Boot**, diseñado para la gestión de conductores y órdenes de transporte.

## Arquitectura del Sistema
* **Eureka Server:** Servidor de descubrimiento (Puerto: `8761`).
* **API Gateway:** Punto de entrada único y enrutador (Puerto: `8090`).
* **Driver MSV:** Gestión de conductores y disponibilidad (Puerto: `8081`).
* **Order MSV:** Gestión de órdenes de transporte (Puerto: `8082`).
* **Commons DTO:** Librería compartida para modelos, mapeos y excepciones globales.
* 
## Tecnologías y Herramientas Utilizadas
* **Backend:** Java 17 & Spring Boot 3.5.x
* **Base de Datos:** Oracle Database (Esquemas separados para DRIVER y ORDERS)
* **Contenerización:** Docker & Docker Compose
* **Comunicación Interna:** OpenFeign & Eureka Discovery Service
* **Pruebas y API:**
  * **Documentación Swagger:** [Driver MSV](http://localhost:8081/swagger-ui/index.html#/) | [Order MSV](http://localhost:8082/swagger-ui/index.html#/)
  * **Colección Postman:** [Pruebas de la API AIT](https://www.postman.com/carlosworkspace/workspace/prueba-ait)

## Guía de Despliegue

### 1. Compilación de Artefactos (.jar)
Para iniciar, es necesario compilar el código fuente. Desde tu terminal (CMD o PowerShell), ejecuta los siguientes comandos de Maven asegurándote de estar dentro de cada carpeta correspondiente:

```bash
# 1. Instalar la librería compartida (Ejecutar dentro de la carpeta /commons-dto)
./mvnw clean install -DskipTests

# 2. Empaquetar los servicios (Ejecutar dentro de /driver-msv, /order-msv, /EurekaServer y /gateway)
./mvnw clean package -DskipTests
```
### 2. Ejecución de Contenedores con Docker
Una vez generados todos los ejecutables .jar de los microservicios, regresa a la carpeta raíz del proyecto (donde se encuentra el archivo docker-compose.yml) y ejecuta:

```bash
# 1. Ejecuta
docker-compose up -d --build
