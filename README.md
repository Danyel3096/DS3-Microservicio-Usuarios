# Microservicio de usuarios

Microservicio de usuarios para la autenticación y gestión de usuarios.

## Requisitos previos

Antes de ejecutar el microservicio, asegúrate de tener instalado:

- Java 17 o superior
- Maven 3.8+

## Construcción del proyecto

Para compilar el proyecto, ejecuta:

```sh
mvn clean install
```

## Ejecución del microservicio

Para iniciar el microservicio, usa el siguiente comando:

```sh
mvn spring-boot:run
```

## Ejecución de pruebas

Para ejecutar las pruebas del proyecto, usa el siguiente comando:

```sh
mvn test
```

## Generación de JAR ejecutable

Si deseas generar un JAR ejecutable, usa:

```sh
mvn package
```

Luego, puedes ejecutar el JAR con:

```sh
java -jar target/tu-archivo.jar
```

## Documentación de la API

La documentación de la API está disponible en Swagger. Una vez que el microservicio esté en ejecución, puedes acceder a ella en la siguiente URL:

[http://localhost:8081/swagger-ui/index.html](http://localhost:8081/swagger-ui/index.html)