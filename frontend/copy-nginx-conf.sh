#!/bin/bash

pwd
cd /usr/share/nginx
if [ -z "$BANKPLUS_CUSTOMERS_SERVICE_HOST" ]; then
    echo "Copying default nginx config"
    mv ./nginx-conf/default.conf /etc/nginx/nginx.conf
else
    echo "Copying OpenShift v3 nginx config"
    /usr/bin/sed -i "s/<customers_host_placeholder>/${BANKPLUS_CUSTOMERS_SERVICE_HOST}/" ./nginx-conf/openshift.conf
    /usr/bin/sed -i "s/<transactions_host_placeholder>/${BANKPLUS_TRANSACTIONS_SERVICE_HOST}/" ./nginx-conf/openshift.conf
    /usr/bin/sed -i "s/<reporting_host_placeholder>/${BANKPLUS_REPORTING_SERVICE_HOST}/" ./nginx-conf/openshift.conf
    mv ./nginx-conf/openshift.conf /etc/nginx/nginx.conf
fi

