# Inventario Productos Web

Aplicación web CRUD para gestión de productos con arquitectura en capas usando Spring Boot.

## Tecnologías
- Java 17
- Spring Boot 3.3.4
- Spring Web
- Spring Data JPA
- Hibernate ORM
- MySQL
- Swagger / OpenAPI
- Docker Compose

## Estructura en capas
- controller
- service
- repository
- model
- dto

## Ejecutar la base de datos con Docker
```bash
docker-compose up -d
```

## Ejecutar la aplicación
```bash
mvn spring-boot:run
```

## Swagger
- URL: http://localhost:8080/swagger-ui.html

## Endpoints principales
- GET /api/productos
- GET /api/productos/{id}
- GET /api/productos/buscar?nombre=
- POST /api/productos
- PUT /api/productos/{id}
- DELETE /api/productos/{id}
