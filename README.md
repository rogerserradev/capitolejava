# 🧪 Prueba Técnica – Sistema de Productos con Precios Históricos

## 🧩 Desarrollo

Esta prueba ha sido elaborada con el siguiente stack tecnológico:

- Java 21
- Springboot 3.3.5
- Spring Jdbc (jdbctemplate)
- Docker
- Docker Compose
- Postgresql, imagen postgres:18.3-alpine
- Gradle
- OpenAPI

He optado por Jdbctemplate, ya que es más optimo que alternativas como JPA, debido a la flexibilidad que tiene al permitir mejor redacción de queries y no depender de Entities.
Además, sabía que se utilizaba en la empresa de antemano, ya que lo pregunté en el Filtro Técnico.
Opté por SQL en lugar de NoSQL debido a que ofrece modelos más consistentes, aunque en casos concretos como el historial de precios, sí que hubiera preferido MongoDB, ya que los datos que se leen juntos, quizá deberían guardarse juntos; en este caso en un documento JSON.
Añadí scripts para introducir datos de prueba en tablas, ya que facilitó el testing de las funcionalidades.

Sigue una arquitectura típica:
- Controller
- Service
- Repository
- Separación de clases java contenedoras, en Request y Response, dependiendo de los datos a devolver

Para levantar la aplicación, basta con ejecutar el comando docker compose up --build
Para hacer una nueva build y aplicar cambios java, ejecutar el comando docker compose down -v

Nota: los datos referentes a base de datos, usuario, contraseña y derivados han sido ubicados en un .env, por lo que subí un .env.interview alternativo

Link a la documentación OpenApi: http://localhost:8080/swagger-ui/index.html

Me he tomado la molestia de adjuntar una colección de Postman con algunas operaciones varias en el directorio postman del proyecto

### 🧩 Especificaciones sobre los requisitos

1. **Crear un producto**

El controlador recibe una petición al endpoint /products y guarda el producto en base de datos.

2. **Agregar un precio a un producto**

El controlador recibe una petición al endpoint /products/{productId}/prices, y el servicio se encarga de validar fechas, comprobar que el producto existe y otras comprobaciones antes de grabar en base de datos.

3. **Obtener el precio vigente de un producto en una fecha**

El controlador recibe una petición al endpoint /products/{productId}/prices, y el servicio se encarga de devolver el precio vigente a una fecha.

4. **Obtener el historial completo de precios de un producto**

En este apartado tuve ambigüedad de url, por lo que tuve que añadir un sufijo, quedando la url así: /products/{productId}/history.
Este fue quizá el apartado que tuve que meditar más, ya que estaba la opción de hacer una sola query con 1 join, o bien mantener 2 queries, una con el producto y otra con el listado de precios.
Como ya tenía RowMapper montado y la separación entre product y price estaba muy marcada, opté por montar un nuevo DTO con las 2 queries, ya que también existía la posibilidad de que no hubiera precios para un producto todavía.
Es un apartado que me gustaría comentar para recibir feedback/mejoras en la revisión de código.

**Extras**

Opté por añadir borrados y actualizaciones de precios, y esto explica por qué ya no uso {id} en las url, ya que apareció el endpoint /{productId}/prices/{priceId}, por lo que por cladidad del código decidí renombrar.
Como comenté anteriormente, añadí un script SQL para añadir datos de productos y precios, para facilitar las pruebas.
También añadí documentación OpenApi, a la cual se puede acceder con el link visto anteriormente.
Toda la lógica de negocio del servicio ha sido testeada con Junit y Mockito.
Barajé la posibilidad de introducir JWT, y usar la información de localización para la divisa, pero es algo que no he usado a nivel profesional, ya que los tokens siempre los proveía otro departamento y nosotros nos limitábamos a usarlo con Postman.
Por último, no realicé las pruebas de performance con herramientas como Gatling, ya que no lo consideré algo esencial debido a que comenté previamente que no poseo experiencia en este campo, aunque estaba dispuesto a aprenderlo si el puesto lo requiere.
En conclusión, todo lo que aparece en el proyecto son las herramientas con las que siempre he trabajado y puedo demostrar experiencia.
Como último apunte, esta prueba me ha servido para afianzar conocimientos que ya tenía con anterioridad (como docker compose), pero que no había utilizado a nivel profesional, debido a que la orquestación de despliegues se realizaba principalmente con Jenkins.

## 🧩 Contexto

Tu objetivo es diseñar e implementar una API que permita gestionar productos y sus precios históricos. Cada producto puede tener múltiples precios a lo largo del tiempo, pero solo un precio puede estar vigente para una misma fecha.

---

## 🎯 Objetivo

Queremos que demuestres tus conocimientos técnicos, tu criterio para tomar decisiones de diseño, y tu capacidad para resolver un problema realista de backend.

Puedes usar el **framework que prefieras**, la **arquitectura que consideres apropiada** y la **base de datos que mejor se adapte a tu solución**. Algunas opciones válidas incluyen Spring Boot, Quarkus, Java puro, PostgreSQL, MongoDB, MySQL, H2, etc.

La implementación puede realizarse en **Java o Kotlin**.

⚠️ **Uno de los requisitos más importantes de esta prueba es que tu solución tenga el mejor rendimiento posible**, tanto en tiempo de respuesta como en uso eficiente de recursos.

---

## 📘 Requisitos funcionales

### Endpoints obligatorios

Debes implementar los siguientes endpoints:

1. **Crear un producto**
    - `POST /products`
    - Body:
      ```json
      {
        "name": "Zapatillas deportivas",
        "description": "Modelo 2025 edición limitada"
      }
      ```

2. **Agregar un precio a un producto**
    - `POST /products/{id}/prices`
    - Body:
      ```json
      {
        "value": 99.99,
        "initDate": "2024-01-01",
        "endDate": "2024-06-30"
      }
      ```
    - Reglas:
        - No debe haber solapamiento de fechas con otros precios del mismo producto.
        - `endDate` puede ser `null`.
        - Validar que `initDate` < `endDate` si ambas existen.

3. **Obtener el precio vigente de un producto en una fecha**
    - `GET /products/{id}/prices?date=2024-04-15`
    - Body:
      ```json
      {
        "value": 99.99
      }
      ```

4. **Obtener el historial completo de precios de un producto**
    - `GET /products/{id}/prices`
    - Body:
      ```json
      {
        "name": "Zapatillas deportivas",
        "description": "Modelo 2025 edición limitada",
        "prices": [
          {
            "value": 99.99,
            "initDate": "2024-01-01",
            "endDate": "2024-06-30"
          },
          {
            "value": 199.99,
            "initDate": "2025-01-01",
            "endDate": "2025-06-30"
          },
        ]
      }
      ```

📌 **Nota**:  
Los endpoints anteriores se utilizarán en las pruebas automáticas.  
Sin embargo, **si consideras que alguno puede mejorarse para alinearse mejor con la semántica REST**, puedes hacerlo libremente, justificándolo en el README de tu proyecto.

---

## ✅ Criterios de evaluación

- Modelado correcto de entidades y relaciones.
- Validación robusta de reglas de negocio.
- Diseño RESTful claro y consistente.
- Organización del código y buenas prácticas.
- Elección justificada del stack técnico.
- **Rendimiento**: arranque rápido, respuestas ágiles, bajo uso de recursos.
- Tests automatizados (unitarios o de integración).
- Claridad en la documentación y facilidad de ejecución.

---

## 🚀 Desafíos opcionales (bonus)

### 1. Prueba de rendimiento automatizada

Puedes incluir una prueba automática de performance para validar el comportamiento de tu API bajo carga.

#### ¿Qué debes entregar?

- Un archivo `docker-compose.yml` que:
    - Levante tu aplicación.
    - Ejecute un script o herramienta (por ejemplo, Gatling, k6, Artillery, JMeter, etc.) con múltiples peticiones concurrentes.

#### ¿Qué se evaluará?

- Tiempo de arranque de la aplicación.
- Velocidad de ejecución de los endpoints.
- Peticiones exitosas por segundo.
- Uso de recursos bajo carga.

#### Restricciones importantes:

- **No se podrán modificar los valores de CPU ni memoria del contenedor de la aplicación ni del script de rendimiento**.
- **Puedes añadir nuevos contenedores auxiliares**, siempre que **cada uno tenga un máximo de 1 GB de memoria y 500 Mi de CPU**.

Esto te permite aplicar estrategias como separación de servicios, caché, balanceo, precálculo, etc., **pero dentro de restricciones razonables de infraestructura**.

---

### 2. Otros desafíos opcionales

- Soporte para múltiples monedas por precio.
- Endpoint para actualizar o eliminar precios.
- Autenticación básica o con token.
- Documentación con Swagger/OpenAPI.
- Scripts para poblar datos de prueba automáticamente.
- Soporte para paginación, ordenamiento o filtrado en el historial de precios.

---

## 📦 Entrega

### El `README.md` debe incluir:

- Instrucciones para compilar y ejecutar el proyecto.
- Justificación de decisiones técnicas.
- Indicaciones si agregaste mejoras, asumiste supuestos o cambiaste los endpoints.
- Cómo ejecutar la prueba de rendimiento (si aplicaste ese desafío).
- Para evitar copias preferimos que nos mandes un zip o nos envíes invitación de un repositorio PRIVADO de Github al contacto que te pasó la prueba.

---

¡Buena suerte! Queremos ver cómo piensas, no solo cómo codificas.
