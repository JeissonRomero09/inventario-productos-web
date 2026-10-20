# Sistema de Gestión de Inventario de Productos Tecnológicos

## 1. Descripción del proyecto

El Sistema de Gestión de Inventario de Productos Tecnológicos es una aplicación web diseñada para facilitar la administración y el control de los productos tecnológicos registrados en un inventario.

La aplicación busca permitir el registro, la consulta, la actualización y la eliminación de productos, así como el control de las cantidades disponibles y el registro de los movimientos de inventario. Con ello, se pretende mejorar la organización de la información, mantener la consistencia de los datos y facilitar el seguimiento de las entradas y salidas de productos.

El proyecto se desarrollará utilizando Java y Spring Boot para la lógica de la aplicación, MySQL como sistema de gestión de bases de datos y Maven para administrar las dependencias y la construcción del proyecto.

## 2. Objetivo general

Desarrollar una aplicación web para gestionar el inventario de productos tecnológicos mediante operaciones de registro, consulta, actualización y eliminación, incorporando mecanismos para controlar las cantidades disponibles y registrar los movimientos de inventario.

## 3. Objetivos específicos

* Diseñar un modelo de clases que represente los elementos principales del negocio.
* Implementar las operaciones CRUD para la administración de los productos.
* Diseñar una base de datos relacional para almacenar la información del inventario.
* Implementar servicios REST para permitir la comunicación entre la interfaz web y la aplicación.
* Registrar los movimientos de entrada y salida de productos.
* Validar la información ingresada para evitar inconsistencias en el inventario.
* Realizar pruebas para verificar el funcionamiento de las funcionalidades implementadas.
* Documentar la arquitectura, las funcionalidades y la configuración del proyecto.

## 4. Tecnologías utilizadas

| Tecnología      | Descripción                                                          |
| --------------- | -------------------------------------------------------------------- |
| Java            | Lenguaje de programación principal.                                  |
| Spring Boot     | Framework para desarrollar la aplicación web y los servicios REST.   |
| Maven           | Herramienta para administrar dependencias y construir el proyecto.   |
| MySQL           | Sistema de gestión de bases de datos relacionales.                   |
| XAMPP           | Entorno local utilizado para ejecutar el servicio MySQL.             |
| Spring Data JPA | Herramienta para facilitar la persistencia de datos.                 |
| Hibernate       | Tecnología de mapeo objeto-relacional utilizada por JPA.             |
| Git             | Sistema de control de versiones.                                     |
| GitHub          | Plataforma para alojar el código fuente y facilitar la colaboración. |
| Draw.io         | Herramienta para elaborar los diagramas del sistema.                 |
| Postman         | Herramienta para probar los servicios REST.                          |

## 5. Funcionalidades del sistema

### 5.1. Gestión de productos

* Registrar productos tecnológicos.
* Consultar los productos registrados.
* Buscar productos por su identificador.
* Actualizar la información de los productos.
* Eliminar productos cuando las reglas del negocio lo permitan.

### 5.2. Gestión del inventario

* Consultar las cantidades disponibles.
* Registrar entradas de productos.
* Registrar salidas de productos.
* Consultar el historial de movimientos.
* Validar que las salidas no superen las cantidades disponibles.
* Mantener la consistencia de las cantidades registradas.

Las funcionalidades se implementarán progresivamente durante el desarrollo del proyecto.

## 6. Requisitos previos

Para configurar el entorno de desarrollo se requieren las siguientes herramientas:

* **Java JDK:** versión compatible con la versión de Spring Boot seleccionada.
* **Entorno de desarrollo:** IntelliJ IDEA, Eclipse o Visual Studio Code con las extensiones correspondientes.
* **Maven:** herramienta para gestionar las dependencias y construir la aplicación. También se puede utilizar Maven Wrapper si está incluido en el proyecto.
* **XAMPP:** para ejecutar el servicio MySQL en el entorno local.
* **Git:** para administrar las versiones del código fuente.
* **GitHub:** para alojar el repositorio del proyecto.
* **Postman:** para realizar pruebas de los servicios REST.
* **Draw.io:** para elaborar los diagramas de clases y arquitectura.

## 7. Instalación y configuración del entorno

### Paso 1. Instalar Java JDK

Instalar una versión del JDK compatible con el proyecto.

Después de la instalación, abrir una terminal y ejecutar:

```bash
java -version
```

Este comando permite verificar que Java esté instalado correctamente.

Si Maven está instalado de forma independiente, comprobar su disponibilidad con:

```bash
mvn -version
```

### Paso 2. Instalar el entorno de desarrollo

Instalar el IDE seleccionado por el equipo y configurarlo para trabajar con Java y Spring Boot.

El entorno debe permitir importar proyectos Maven, ejecutar la aplicación y visualizar los resultados de las pruebas.

### Paso 3. Crear el proyecto Spring Boot

Generar el proyecto mediante Spring Initializr o desde el IDE seleccionado.

Configurar los siguientes elementos:

* Lenguaje: Java.
* Herramienta de construcción: Maven.
* Empaquetado: JAR.
* Versión de Java: compatible con Spring Boot.
* Dependencias: Spring Web, Spring Data JPA y el controlador de MySQL.

La selección definitiva de dependencias dependerá de las funcionalidades que se implementen.

### Paso 4. Obtener el proyecto desde GitHub

Clonar el repositorio utilizando Git:

```bash
git clone URL_DEL_REPOSITORIO
```

Ingresar a la carpeta del proyecto:

```bash
cd NOMBRE_DEL_PROYECTO
```

Se debe reemplazar `URL_DEL_REPOSITORIO` por la dirección real del repositorio y `NOMBRE_DEL_PROYECTO` por el nombre de la carpeta creada.

### Paso 5. Instalar y configurar MySQL con XAMPP

Instalar XAMPP e iniciar el servicio MySQL desde su panel de control.

Abrir phpMyAdmin y crear la base de datos que utilizará la aplicación.

Ejemplo de creación:

```sql
CREATE DATABASE inventario_tecnologico;
```

El nombre utilizado en este ejemplo es una propuesta. Debe coincidir con el nombre configurado en la aplicación.

### Paso 6. Configurar la conexión a la base de datos

Abrir el archivo:

`src/main/resources/application.properties`

Configurar los parámetros de conexión:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/inventario_tecnologico
spring.datasource.username=USUARIO_MYSQL
spring.datasource.password=CONTRASENA_MYSQL

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

Se deben reemplazar `USUARIO_MYSQL` y `CONTRASENA_MYSQL` por las credenciales correspondientes al entorno local.

La propiedad `spring.jpa.hibernate.ddl-auto=update` debe utilizarse únicamente si coincide con la estrategia de gestión del esquema elegida por el equipo. En otros entornos pueden utilizarse scripts SQL o herramientas de migración.

Las contraseñas y demás credenciales no deben publicarse en el repositorio.

### Paso 7. Instalar las dependencias del proyecto

Desde la carpeta raíz, ejecutar:

```bash
mvn clean install
```

Si el proyecto incluye Maven Wrapper, en Windows se puede utilizar:

```bash
mvnw.cmd clean install
```

Este proceso descarga las dependencias necesarias, compila el código y ejecuta las pruebas configuradas.

### Paso 8. Ejecutar la aplicación

Iniciar la aplicación mediante Maven:

```bash
mvn spring-boot:run
```

Si el proyecto incluye Maven Wrapper, en Windows se puede utilizar:

```bash
mvnw.cmd spring-boot:run
```

Si la aplicación utiliza el puerto predeterminado de Spring Boot, normalmente se podrá acceder al servidor local desde:

```text
http://localhost:8080
```

Las rutas disponibles dependerán de los controladores implementados. La dirección anterior corresponde al servidor y no necesariamente a una página web específica.

### Paso 9. Probar los servicios REST

Utilizar Postman para enviar solicitudes HTTP a los endpoints implementados.

Comprobar que las respuestas sean correctas y que las operaciones realizadas se reflejen en la base de datos MySQL.

## 8. Estructura preliminar del proyecto

La organización propuesta para el código fuente y la documentación es la siguiente:

```text
inventario-tecnologico/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── paquete/
│   │   │       ├── controller/
│   │   │       ├── service/
│   │   │       ├── model/
│   │   │       └── repository/
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/
├── docs/
│   ├── diagrama_clases.drawio
│   ├── diagrama_clases.png
│   ├── diagrama_arquitectura.drawio
│   ├── diagrama_arquitectura.png
│   └── servicios_rest.md
├── pom.xml
├── README.md
└── .gitignore
```

Esta estructura es preliminar. Los nombres de los paquetes y archivos se ajustarán a la organización definitiva del proyecto.

## 9. Modelo del negocio

El modelo del negocio contempla inicialmente las siguientes clases:

### 9.1. Producto

Representa cada producto tecnológico registrado en el inventario.

Atributos propuestos:

* `id`: identificador del producto.
* `nombre`: nombre del producto.
* `descripcion`: descripción de sus características.
* `precio`: precio del producto.
* `cantidad`: unidades disponibles en inventario.

### 9.2. MovimientoInventario

Representa las entradas y salidas de productos.

Atributos propuestos:

* `id`: identificador del movimiento.
* `tipo`: tipo de movimiento, entrada o salida.
* `cantidad`: número de unidades involucradas.
* `fecha`: fecha y hora del movimiento.
* `observacion`: información adicional del movimiento.

Un producto puede tener varios movimientos de inventario. Cada movimiento estará asociado con el producto correspondiente.

Estas clases son una propuesta inicial y deben ajustarse a los requisitos definitivos del sistema.

## 10. Servicios REST previstos

Los servicios REST permitirán la comunicación entre la interfaz web y la lógica de negocio.

| Método HTTP | Endpoint                          | Descripción                                                     |
| ----------- | --------------------------------- | --------------------------------------------------------------- |
| POST        | `/api/productos`                  | Registrar un producto.                                          |
| GET         | `/api/productos`                  | Consultar todos los productos.                                  |
| GET         | `/api/productos/{id}`             | Consultar un producto por su identificador.                     |
| PUT         | `/api/productos/{id}`             | Actualizar la información de un producto.                       |
| DELETE      | `/api/productos/{id}`             | Eliminar un producto cuando las reglas del negocio lo permitan. |
| POST        | `/api/movimientos`                | Registrar una entrada o salida de inventario.                   |
| GET         | `/api/movimientos`                | Consultar los movimientos registrados.                          |
| GET         | `/api/productos/{id}/movimientos` | Consultar los movimientos de un producto.                       |

Los endpoints anteriores corresponden al diseño preliminar. Las rutas definitivas dependerán de la implementación de los controladores REST.

## 11. Pruebas y validación

Durante el desarrollo se realizarán pruebas para verificar los siguientes aspectos:

* Registro correcto de productos.
* Consulta de productos individuales y listados.
* Actualización de los datos registrados.
* Eliminación de productos conforme a las reglas del negocio.
* Validación de campos obligatorios.
* Validación de valores numéricos.
* Registro de entradas y salidas de inventario.
* Prevención de salidas superiores a las cantidades disponibles.
* Persistencia y consistencia de los datos en MySQL.
* Respuestas adecuadas de los servicios REST ante solicitudes válidas e inválidas.

Las pruebas podrán ejecutarse mediante Postman y las herramientas de pruebas de Java disponibles en el proyecto.

## 12. Documentación y diagramas

La carpeta `docs/` contendrá la documentación técnica y los diagramas elaborados durante el diseño del sistema.

Se contemplan los siguientes elementos:

* **Diagrama de clases:** representa las clases del modelo del negocio, sus atributos, operaciones y relaciones.
* **Diagrama de arquitectura:** representa la interfaz web, los controladores REST, los servicios, los repositorios y la base de datos.
* **Documentación de servicios REST:** describe las rutas, los métodos HTTP y el propósito de los endpoints.

Los diagramas se elaborarán utilizando Draw.io y se conservarán tanto en formato editable como en formato de imagen.

## 13. Repositorio de GitHub

El código fuente y la documentación del proyecto se alojarán en un repositorio de GitHub para facilitar el control de versiones, la colaboración entre los integrantes y el seguimiento del desarrollo.

**Enlace del repositorio:** [Agregar aquí la URL real del repositorio de GitHub].

En el repositorio se incluirán el código fuente, los archivos de configuración necesarios, la documentación y los diagramas del sistema.

## 14. Integrantes del equipo

El proyecto será desarrollado por los siguientes integrantes:

1. **Jeisson Jesús Romero Mariño**
2. **Cristian Camilo Vera Botero**

Ambos integrantes participarán en las actividades de análisis, diseño, desarrollo, documentación y validación del proyecto, de acuerdo con la distribución de responsabilidades definida por el equipo.

## 15. Estado del proyecto

El proyecto se encuentra en fase de diseño y desarrollo. Se contempla la configuración del entorno, el diseño del modelo del negocio, la construcción de la arquitectura de la aplicación web y la implementación progresiva de las funcionalidades de inventario.

La documentación se actualizará conforme se avance en el desarrollo y se validen las funcionalidades.