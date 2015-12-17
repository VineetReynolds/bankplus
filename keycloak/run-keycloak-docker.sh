#!/bin/bash

if [ $# -ne 1 ]
then
    echo "Usage - $0   DIR"
    echo "        Where DIR is the BankPlus Keycloak provider directory with distribution"
    exit 1
elif [[ -d "$1" ]]
then
    pushd "$1" >/dev/null
    DIR=`pwd`
    echo "Building BankPlus Keycloak provider..."
    mvn clean package
    echo "Building Keycloak Docker container with BankPlus provider...."
    docker build -t bankplus-keycloak .
    echo "Running Keycloak Docker container with BankPlus provider...."
    docker run -p 8080:8080 -d bankplus-keycloak
    popd >/dev/null
fi
    
