#!/bin/bash
mvn clean package
java -jar target/engine-takehome-1.0-SNAPSHOT.jar < src/test/resources/sample_input.csv
