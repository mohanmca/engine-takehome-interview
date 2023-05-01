#!/bin/bash
mvn package
java -jar target/engine-takehome-1.0-SNAPSHOT.jar < ./sample_input.txt