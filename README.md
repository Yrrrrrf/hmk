<h1 align="center">
  <img src="https://cdn-icons-png.flaticon.com/512/1087/1087841.png" alt="¿Cuántas Hamburguesas? Icon" width="128" height="128">
  <div align="center">¿Cuántas Hamburguesas?</div>
</h1>

<div align="center">

[![GitHub: Repo](https://img.shields.io/badge/Proyecto-Hamburguesas-58A6FF?&logo=github)](https://github.com/Yrrrrrf/hmk)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow)](./LICENSE)
[![Version](https://img.shields.io/badge/version-0.1.0-blue.svg)](https://github.com/Yrrrrrf/hmk/releases)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.0.20-orange?logo=kotlin)](https://kotlinlang.org/)
[![Docker](https://img.shields.io/badge/Docker-Supported-2496ED?logo=docker)](https://www.docker.com/)

</div>

¿Cuántas Hamburguesas? es una aplicación web de adivinanza desarrollada en una arquitectura híbrida con servlets de Kotlin como backend y una API de FastAPI en Python como capa de datos, todo ello integrado con una base de datos PostgreSQL. El juego permite a los usuarios registrar sus puntajes y competir en tablas de clasificación.

## 🛠️ Stack Tecnológico

-   **Backend**: Kotlin con Servlets para la lógica de negocio, complementado con un cliente HTTP Ktor.
-   **API**: Python con FastAPI para la generación automática de API REST, utilizando `prism-py`.
-   **Base de Datos**: PostgreSQL, completamente contenedorizado con Docker.
-   **Frontend**: JSP con HTML, CSS y JavaScript para la interfaz de usuario.
-   **Arquitectura**: Aplicación de múltiples capas con contenedores Docker.

## 🚦 Inicio Rápido

### Prerrequisitos

-   [Docker](https://www.docker.com/) y Docker Compose
-   [Java 21](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html) (para desarrollo con Kotlin)
-   [Kotlin 2.0.20](https://kotlinlang.org/) (para desarrollar con el backend)

### Ejecutar la Aplicación

1.  **Revisar el Archivo de Entorno**: Un archivo `.env` ha sido creado en la raíz del proyecto con las credenciales de la base de datos. Puedes modificarlo si es necesario.

2.  **Iniciar los servicios usando Docker Compose**:
    ```sh
    docker compose up -d
    ```

3.  **Acceder a los Servicios**:
    -   **Documentación de la API**: [http://localhost:8000/docs](http://localhost:8000/docs)
    -   **Aplicación Web**: [http://localhost:8080](http://localhost:8080) (o el puerto configurado para el servidor servlet)

## 📄 Licencia

Este proyecto está licenciado bajo la **[Licencia MIT](./LICENSE)**.