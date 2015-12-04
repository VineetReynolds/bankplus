#!/bin/bash

if [ $# -ne 1 ]
then
    echo "Usage - $0   DIR"
    echo "        Where DIR is the BankPlus Customers service directory"
    exit 1
elif [[ -d "$1" ]]
then
    pushd "$1" >/dev/null
    DIR=`pwd`
    echo "Building BankPlus Customers service..."
    mvn clean package
    echo "Building BankPlus Customers service Docker container...."
    docker build -t bankplus-customers .
    echo "Running BankPlus Customers service Docker container...."
    docker run --name customers -p 8080:8080 -d --link accounting:accounting bankplus-customers
    popd >/dev/null
fi
    
