# Instrucciones para usar ¿Cuántas Hamburguesas?

## Usuarios de ejemplo
La aplicación viene con usuarios predefinidos para probarla:

- **Usuario**: player1, **Contraseña**: pass1
- **Usuario**: player2, **Contraseña**: pass2
- **Usuario**: player3, **Contraseña**: pass3
- **Usuario**: player4, **Contraseña**: pass4

## Cómo ejecutar la aplicación

### Requisitos
- Tener Docker y Docker Compose instalados

### Pasos
1. Abre una terminal en la carpeta del proyecto
2. Ejecuta el comando: `docker compose up -d`
3. Espera a que se inicien todos los servicios

### Acceso a la aplicación
- **Juego**: [http://localhost:8080](http://localhost:8080)
- **Documentación de la API**: [http://localhost:8000/docs](http://localhost:8000/docs)

### Cómo jugar
1. Accede a [http://localhost:8080](http://localhost:8080)
2. Inicia sesión con uno de los usuarios de ejemplo
3. Adivina el número secreto (entre 1 y 100)
4. El juego te dirá si tu número es mayor o menor
5. ¡Trata de adivinarlo en la menor cantidad de intentos!

### Puertos usados
- Puerto web: 8080
- Puerto API: 8000
- Puerto base de datos: 5432