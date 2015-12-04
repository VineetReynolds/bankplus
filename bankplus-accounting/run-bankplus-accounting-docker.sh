#!/bin/bash

if [ $# -ne 1 ]
then
    echo "Usage - $0   DIR"
    echo "        Where DIR is the BankPlus Accounting service directory"
    exit 1
elif [[ -d "$1" ]]
then
    pushd "$1" >/dev/null
    DIR=`pwd`
    echo "Building BankPlus Accounting service..."
    mvn clean package
    echo "Building BankPlus Accounting service Docker container...."
    docker build -t bankplus-accounting .
    echo "Running BankPlus Accounting service Docker container...."
    docker run --name accounting -p 8080:8080 -d bankplus-accounting
    popd >/dev/null
fi
    
