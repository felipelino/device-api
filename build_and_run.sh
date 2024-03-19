#!/bin/bash

mvn clean package
java -jar ./target/device-api-0.0.1-SNAPSHOT.jar