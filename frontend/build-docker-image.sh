#!/bin/bash

grunt build
docker build -t bankplus-frontend .
