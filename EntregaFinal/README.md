# NexoStock — Inventario web

Aplicación web en Java para administrar un inventario de artículos tecnológicos. Incluye catálogo, entradas y salidas de mercancía, historial, alertas de stock bajo y valoración del inventario en COP.

## Requisitos

- JDK 17 o superior.
- XAMPP con MySQL habilitado.
- Maven 3.9 o superior.

## Preparar MySQL en XAMPP

1. Inicia los módulos **Apache** (opcional, para phpMyAdmin) y **MySQL** desde XAMPP.
2. Abre phpMyAdmin en `http://localhost/phpmyadmin`.
3. Importa [`database/Inventario.sql`](database/Inventario.sql). El script crea la base `Inventario` y las tablas `products` e `inventory_movements`.
4. La configuración predeterminada usa `localhost:3306`, usuario `root` y contraseña vacía, que corresponde a una instalación local habitual de XAMPP.

Si tu instancia usa otros datos, configura estas variables antes de ejecutar:

| Variable | Valor predeterminado |
| --- | --- |
| `DB_URL` | `jdbc:mysql://localhost:3306/Inventario?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=America/Bogota` |
| `DB_USERNAME` | `root` |
| `DB_PASSWORD` | vacío |
| `PORT` | `8081` |

En PowerShell, por ejemplo:

```powershell
$env:DB_PASSWORD = "tu-clave-local"
mvn spring-boot:run
```

## Ejecutar

Desde esta carpeta:

```powershell
java -jar target\inventario-web-1.0.0.jar
```

También se puede iniciar en desarrollo con `mvn spring-boot:run`. Abre [http://localhost:8081](http://localhost:8081). La primera ejecución requiere que el servicio MySQL de XAMPP esté activo y que el script de base de datos se haya importado. Hibernate valida el esquema existente; no altera ni borra las tablas.

Para comprobar la lógica de inventario:

```powershell
mvn test
```

## Funcionalidades

- Crear, editar, buscar, filtrar y eliminar artículos.
- Registrar entradas y salidas de unidades con nota opcional.
- Bloquear salidas superiores al stock disponible sin guardar cambios parciales.
- Consultar el historial general y por artículo.
- Ver alertas basadas en el umbral mínimo configurable de cada artículo.
- Consultar unidades existentes y el valor total del stock (`precio unitario × existencias`) en pesos colombianos.
- API REST disponible bajo `/api`; el detalle de endpoints y el patrón se documentan en [`docs/diseno-patron-strategy.md`](docs/diseno-patron-strategy.md).

## Decisiones de diseño

La aplicación organiza el backend en capas —controlador REST, servicio, dominio y repositorios JPA— y utiliza el patrón GoF **Strategy** para encapsular las reglas de entradas y salidas. El diagrama de clases y la arquitectura actualizados están en [`docs/diseno-patron-strategy.md`](docs/diseno-patron-strategy.md).
