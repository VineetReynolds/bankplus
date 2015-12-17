#!/bin/bash

mvn clean package
docker build -t bankplus-keycloak .
