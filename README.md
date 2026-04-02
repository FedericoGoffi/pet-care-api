# Pet Care API

API REST para gestión de mascotas y vacunaciones, desarrollada con Spring Boot 3 y Java 21.

## Tecnologías

- Java 21
- Spring Boot 3.3.5
- Spring Security + JWT
- MySQL
- Maven
- Springdoc OpenAPI (Swagger)

## Requisitos previos

- Java 21
- MySQL 8+
- Maven (o usar el wrapper `mvnw` incluido)

---

## Instalación y ejecución local

### 1. Clonar el repositorio

```bash
git clone https://github.com/FedericoGoffi/pet-care-api.git
cd pet-care-api
```

### 2. Crear la base de datos

La app crea la base automáticamente al arrancar. Solo necesitás tener MySQL corriendo con un usuario que tenga permisos.

### 3. Definir variables de entorno

Solo es obligatoria la contraseña de MySQL si tu usuario root no tiene contraseña vacía:

```powershell
# Windows PowerShell
$env:DB_PASSWORD="tu-contraseña-mysql"
```

```bash
# Linux / Mac
export DB_PASSWORD="tu-contraseña-mysql"
```

El resto de valores tienen defaults seguros para desarrollo y no necesitan definirse.

### 4. Arrancar la API

```powershell
.\mvnw spring-boot:run
```

La API queda disponible en `http://localhost:8080`.

---

## Usuarios de prueba

Al arrancar en modo desarrollo, se crean automáticamente tres usuarios:

| Username | Contraseña          | Rol         |
|----------|---------------------|-------------|
| admin    | CambiaEstoEnProd!99 | ADMIN       |
| vet      | CambiaEstoEnProd!88 | VETERINARIO |
| user     | CambiaEstoEnProd!77 | USER        |

### Crear un usuario desde la API

Cualquier persona puede registrarse con rol USER desde el endpoint de registro:

```http
POST /auth/registro
Content-Type: application/json

{
  "username": "nuevo_usuario",
  "password": "mipassword1"
}
```

### Crear un ADMIN o VETERINARIO

Los roles privilegiados se asignan manualmente en la base de datos. Primero registrá el usuario normalmente, luego ejecutá en MySQL:

```sql
-- Ver los IDs de los roles disponibles
SELECT * FROM roles;

-- Asignar rol VETERINARIO al usuario (reemplazá X e Y por los IDs reales)
INSERT INTO usuario_roles (usuario_id, rol_id)
SELECT u.id, r.id
FROM usuarios u, roles r
WHERE u.username = 'nombre_usuario' AND r.nombre = 'VETERINARIO';

-- Para ADMIN es igual, solo cambiá 'VETERINARIO' por 'ADMIN'
```

---

## Documentación de la API

Con la app corriendo, accedé a Swagger en:

```
http://localhost:8080/swagger-ui/index.html
```

Para probar endpoints protegidos:

1. Hacé `POST /auth/login` o `POST /auth/registro`
2. Copiá el token que devuelve
3. Hacé click en **Authorize** e ingresá el token (sin la palabra "Bearer")
4. Todos los requests siguientes lo usan automáticamente

---

## Endpoints principales

### Autenticación (público)

| Método | Endpoint         | Descripción                        |
|--------|------------------|------------------------------------|
| POST   | /auth/login      | Login — devuelve JWT               |
| POST   | /auth/registro   | Registro — crea usuario con rol USER |

### Mascotas

| Método | Endpoint          | Rol requerido         | Descripción                        |
|--------|-------------------|-----------------------|------------------------------------|
| POST   | /mascotas         | USER                  | Crear mascota                      |
| GET    | /mascotas         | USER                  | Listar mis mascotas                |
| GET    | /mascotas/{id}    | USER                  | Ver mascota por ID                 |
| GET    | /mascotas/todas   | VETERINARIO, ADMIN    | Listar todas (paginado)            |

El endpoint `/mascotas/todas` acepta parámetros de paginación:

```
GET /mascotas/todas?pagina=0&tamanio=20
```

### Vacunaciones

| Método | Endpoint                              | Rol requerido              | Descripción                  |
|--------|---------------------------------------|----------------------------|------------------------------|
| POST   | /vacunaciones/mascotas/{id}           | USER, VETERINARIO, ADMIN   | Aplicar vacuna               |
| GET    | /vacunaciones/mascotas/{id}/vencidas  | USER, VETERINARIO, ADMIN   | Ver vacunas vencidas         |
| GET    | /vacunaciones/mascotas/{id}/por-vencer| USER, VETERINARIO, ADMIN   | Ver vacunas por vencer       |

El endpoint `/por-vencer` acepta el parámetro `dias` (default: 30):

```
GET /vacunaciones/mascotas/1/por-vencer?dias=60
```

### Administración

| Método | Endpoint         | Rol requerido | Descripción       |
|--------|------------------|---------------|-------------------|
| GET    | /api/admin/panel | ADMIN         | Panel de admin    |
| GET    | /actuator/health | ADMIN         | Estado de la app  |

---

## Roles y permisos

| Acción                          | USER | VETERINARIO | ADMIN |
|---------------------------------|------|-------------|-------|
| Registrarse / loguearse         | si   | si          | si    |
| Crear mascota                   | si   | no          | no    |
| Ver sus propias mascotas        | si   | no          | no    |
| Ver todas las mascotas          | no   | si          | si    |
| Aplicar vacuna (propia mascota) | si   | si          | si    |
| Ver vacunas (propia mascota)    | si   | si          | si    |
| Panel de administración         | no   | no          | si    |

> VETERINARIO y ADMIN pueden aplicar y consultar vacunas de cualquier mascota, no solo las propias.

---

## Variables de entorno

| Variable           | Descripción                        | Requerida en prod |
|--------------------|------------------------------------|-------------------|
| DB_URL             | URL de conexión a MySQL            | si                |
| DB_USERNAME        | Usuario de MySQL                   | si                |
| DB_PASSWORD        | Contraseña de MySQL                | si                |
| JWT_SECRET         | Clave secreta para firmar JWT      | si                |
| APP_CORS_ORIGIN    | URL del frontend permitida por CORS| si                |
| APP_ADMIN_PASSWORD | Contraseña del admin inicial (dev) | no                |
| APP_VET_PASSWORD   | Contraseña del vet inicial (dev)   | no                |
| APP_USER_PASSWORD  | Contraseña del user inicial (dev)  | no                |

Para generar un JWT_SECRET seguro:

```bash
openssl rand -base64 64
```

---

## Seguridad

- Contraseñas almacenadas con BCrypt
- Autenticación stateless con JWT (expira en 4 horas)
- Variables sensibles leídas desde variables de entorno
- Usuarios iniciales solo se crean en perfil `dev`
- Swagger deshabilitado en producción
- CORS configurado para un único origen

---

## Estructura del proyecto

```
src/
├── main/
│   ├── java/com/saltoagro/pet_care_api/
│   │   ├── config/          # Configuración de seguridad, CORS, Swagger, datos iniciales
│   │   ├── controller/      # Endpoints REST
│   │   ├── dto/             # Objetos de transferencia de datos
│   │   ├── exception/       # Manejo global de excepciones
│   │   ├── model/           # Entidades JPA
│   │   ├── repository/      # Acceso a datos
│   │   ├── security/        # JWT, filtros, autenticación
│   │   └── service/         # Lógica de negocio
│   └── resources/
│       ├── application.properties
│       ├── application-dev.properties
│       └── application-prod.properties
└── test/
    └── java/                # Tests unitarios
```
