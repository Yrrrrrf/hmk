<h1 align="center">
  <img src="https://cdn-icons-png.flaticon.com/512/1087/1087841.png" alt="¿Cuántas Hamburguesas? Icon" width="128" height="128">
  <div align="center">¿Cuántas Hamburguesas?</div>
</h1>

<div align="center">

[![GitHub: Repo](https://img.shields.io/badge/Proyecto-Hamburguesas-58A6FF?&logo=github)](https://github.com/Yrrrrrf/hmk)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow)](./LICENSE)
[![Version](https://img.shields.io/badge/version-2.0.0-blue.svg)](https://github.com/Yrrrrrf/hmk/releases)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.0.20-orange?logo=kotlin)](https://kotlinlang.org/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-green?logo=springboot)](https://spring.io/projects/spring-boot)
[![Docker](https://img.shields.io/badge/Docker-Supported-2496ED?logo=docker)](https://www.docker.com/)

</div>

**¿Cuántas Hamburguesas?** es una aplicación web moderna desarrollada con
**Spring Boot** y **Kotlin**. Utiliza una arquitectura en capas (Domain-Driven
Design) para desacoplar la lógica de negocio de la infraestructura, garantizando
un código limpio y mantenible.

El juego desafía a los usuarios a adivinar un número secreto, registrando sus
intentos y compitiendo en una tabla de clasificación global en tiempo real.

## 🛠️ Stack Tecnológico

- **Backend**: Kotlin con Spring Boot (Web MVC, Data JPA).
- **Base de Datos**: PostgreSQL 16+, orquestada con Docker.
- **Frontend**: Thymeleaf (Server-Side Rendering) con HTML5, CSS3 Glassmorphism
  y JavaScript modular.
- **Mapeo de Datos**: MapStruct para la conversión eficiente entre Entidades y
  DTOs.
- **Arquitectura**: Diseño en capas (Web, Servicio, Dominio, Persistencia).

## 🚦 Inicio Rápido

### Prerrequisitos

- [Docker](https://www.docker.com/) y Docker Compose.
- [Java 21](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html)
  (JDK).

### Ejecutar la Aplicación

1. **Iniciar los servicios**: El proyecto incluye un archivo
   `docker-compose.yml` que levanta tanto la base de datos como la aplicación.
   ```sh
   docker compose up -d --build
   ```

2. **Acceder a la Aplicación**:
   - **Web UI**: [http://localhost:8080](http://localhost:8080)
   - La base de datos se inicializa automáticamente con el esquema y datos de
     prueba.

## 👥 Usuarios de Ejemplo

El sistema precarga usuarios para facilitar las pruebas:

- **Usuario**: `player1`, **Contraseña**: `pass1`
- **Usuario**: `player2`, **Contraseña**: `pass2`
- **Usuario**: `player3`, **Contraseña**: `pass3`

## 🔌 Endpoints de la API (JSON)

El backend expone una API RESTful documentada a continuación:

| Método   | Endpoint          | Descripción               | Payload / Parámetros                  |
| :------- | :---------------- | :------------------------ | :------------------------------------ |
| `GET`    | `/api/users/all`  | Listar todos los usuarios | N/A                                   |
| `POST`   | `/api/users/save` | Registrar usuario (JSON)  | `{"login": "...", "password": "..."}` |
| `DELETE` | `/api/users/{id}` | Eliminar usuario          | N/A                                   |
| `POST`   | `/api/scores`     | Registrar puntaje         | `{"gameId": 1, "score": 5}`           |
| `GET`    | `/api/scores/top` | Obtener Top 10            | `?gameId=1`                           |

## 🎮 Cómo Jugar

1. Ve a [http://localhost:8080](http://localhost:8080).
2. Inicia sesión (o regístrate).
3. Ingresa un número entre 1 y 100.
4. El sistema te dará pistas (Mayor/Menor).
5. ¡Gana e intenta entrar al Top 3!

## 🔧 Configuración Técnica

- **Puerto Web**: [8080](http://localhost:8080)
- **Puerto BD**: [5432](http://localhost:5432)
- **Seguridad**: Validación de formularios y gestión de sesiones mediante
  `HttpSession`.

## 📄 Licencia

Este proyecto está licenciado bajo la **[Licencia MIT](./LICENSE)**.
