# Sistema de Microservicios - Prueba Técnica AIT

Este ecosistema de microservicios está desarrollado con **Java 17** y **Spring Boot 3**, diseñado para la gestión de conductores y órdenes de transporte. Incluye una arquitectura robusta de microservicios con servidor de descubrimiento y puerta de enlace centralizada.

## Arquitectura del Sistema
* **Eureka Server:** Servidor de descubrimiento (Puerto: 8761).
* **API Gateway:** Punto de entrada único (Puerto: 8090).
* **Driver MSV:** Gestión de conductores y disponibilidad (Puerto: 8081).
* **Order MSV:** Gestión de órdenes de entrega (Puerto: 8082).
* **Commons DTO:** Librería compartida para modelos y excepciones.

## 🛠️ Tecnologías Utilizadas
* **Backend:** Java 17 & Spring Boot 3.5.x
* **Contenerización:** Docker & Docker Compose
* **Base de Datos:** Oracle Database
* **Comunicación:** OpenFeign & Eureka Discovery Service
* **Herramientas:** Maven, Lombok, Spring Doc (Swagger)

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

---


1.  **Sección de Requisitos:** Le dice al evaluador qué necesita tener instalado antes de empezar.
2.  **URLs de Swagger:** Esto es **clave**. Los reclutadores aman ver la documentación de Swagger interactiva; les permite probar tu API en 2 segundos sin abrir Postman.
3.  **URLs de Eureka:** Para que el evaluador compruebe que tus microservicios realmente se están comunicando entre sí.
4.  **Separación de pasos:** Separamos la compilación del despliegue para que el flujo sea más claro.
