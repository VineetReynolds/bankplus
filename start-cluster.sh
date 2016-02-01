#!/bin/bash

# Disable dnsmasq because skydns will bind to port 53
# sudo systemctl stop dnsmasq.service
# sudo systemctl disable dnsmasq.service
docker-compose -p bankplus up -d
