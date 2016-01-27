#!/bin/bash

if [ -z "$BANKPLUS_CUSTOMERS_SERVICE_HOST" ]; then
    echo "Using default nginx config"
    exec /usr/sbin/nginx -c $HOME/nginx-conf/default.conf -g "daemon off;"
else
    echo "Using OpenShift v3 nginx config"
    /usr/bin/sed -i "s/<customers_host_placeholder>/${BANKPLUS_CUSTOMERS_SERVICE_HOST}/" $HOME/nginx-conf/openshift.conf
    /usr/bin/sed -i "s/<transactions_host_placeholder>/${BANKPLUS_TRANSACTIONS_SERVICE_HOST}/" $HOME/nginx-conf/openshift.conf
    /usr/bin/sed -i "s/<reporting_host_placeholder>/${BANKPLUS_REPORTING_SERVICE_HOST}/" $HOME/nginx-conf/openshift.conf
    exec /usr/sbin/nginx -c $HOME/nginx-conf/openshift.conf -g "daemon off;"
fi

