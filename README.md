
# Device API

## Pre-requisites

* Java 17
* Maven 3

## Overview

### Database H2

The Database used is the H2 in memory database.
The project relies on JPA, so it's easy to adapt to any SQL database. Just add the dependency on pom.xml and adjust the configuration properly.

### Spring Actuator

The project enable the Spring Actuator endpoints.
Reference here: https://www.baeldung.com/spring-boot-actuators

## Build 

* Run: `mvn clean package`

## Run locally

* Run: `java -jar ./target/device-api-0.0.1-SNAPSHOT.jar`

You can access the Swagger UI here: http://localhost:8080/swagger-ui/index.html


## Build and Run Script

We have a shell script to build and run the application: `./build_and_run.sh`