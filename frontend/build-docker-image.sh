#!/bin/bash

# Install grunt and other node dependencies
npm install

# Build the project
grunt build

# Build the docker container
docker build -t bankplus-frontend .
