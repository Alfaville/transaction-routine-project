# Pismo - Transaction Routine

The local test API are in [Swagger].

### Stack technology

* Java 11
* Spring Boot 2
* Postgresql
* Lombok
  * Project Lombok is a java library that automatically plugs into your editor and build tools, spicing up your java.
    Never write another getter or equals method again, with one annotation your class has a fully featured builder, Automate your logging variables, and much more.
* Logbook
  * Logbook is an extensible to enable complete request and response logging for different client- and server-side technologies.
* H2
  * H2 is a relational database management system. It can be embedded in Java applications or run in client-server mode.
* JUnit
  * JUnit is a unit testing framework. JUnit has been important in the development of TDD.
* Flyway
  * Flyway is an open-source database-migration tool.

### Installation

* Java 11 minimum version;
* Gradle 4.5 minimum version;
* Docker

### Plugins

* Lombok

### Deployment

* Run the start.sh file and choose the environment (DEV, LOCAL or PROD).

[Swagger]: <http://localhost:9000/transaction-routine/swagger-ui/index.html?configUrl=/transaction-routine/v3/api-docs/swagger-config>