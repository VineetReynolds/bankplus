#!/bin/bash

if [ $# -ne 1 ]
then
    echo "Usage - $0   DIR"
    echo "        Where DIR is the BankPlus frontend directory with distribution and nginx configuration"
    exit 1
elif [[ -d "$1" ]]
then
    pushd "$1" >/dev/null
    DIR=`pwd`
    echo "Building frontend..."
    grunt build
    echo "Initiating nginx Docker container...."
    docker run -v $DIR/dist:/usr/share/nginx/html:ro -v $DIR/nginx-conf:/etc/nginx/conf.d:ro -p 80:80 -d nginx
    popd >/dev/null
fi
    
