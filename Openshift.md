oc new-project keycloak

oc create -f https://raw.githubusercontent.com/VineetReynolds/sti-keycloak/master/9.0/test/imagestream.json
oc create -f https://raw.githubusercontent.com/VineetReynolds/sti-keycloak/master/9.0/test/build-config.json
oc start-build keycloak-9-centos7-build

oc create -f https://raw.githubusercontent.com/wildfly-swarm/sti-wildflyswarm/master/1.0/test/imagestream.json
oc create -f https://raw.githubusercontent.com/wildfly-swarm/sti-wildflyswarm/master/1.0/test/build-config.json
oc start-build wildflyswarm-10-centos7-build

oc policy add-role-to-user -z default view
oc new-app --env="KEYCLOAK_PROVIDER_NAME=org.jboss.examples.bankplus" --name=bankplus-keycloak --context-dir=keycloak keycloak-9-centos7~https://github.com/VineetReynolds/bankplus.git
oc expose service bankplus-keycloak

oc new-app --env="SWARM_JAR=/target/*-swarm.jar" --name=bankplus-accounting wildflyswarm-10-centos7~https://github.com/VineetReynolds/bankplus
oc new-app --env="SWARM_JAR=/target/*-swarm.jar" --name=bankplus-customers wildflyswarm-10-centos7~https://github.com/VineetReynolds/bankplus
oc expose service bankplus-customers
oc new-app --env="SWARM_JAR=/target/*-swarm.jar" --name=bankplus-messaging wildflyswarm-10-centos7~https://github.com/VineetReynolds/bankplus
oc new-app --env="SWARM_JAR=/target/*-swarm.jar" --name=bankplus-reporting wildflyswarm-10-centos7~https://github.com/VineetReynolds/bankplus
oc expose service bankplus-reporting
oc new-app --env="SWARM_JAR=/target/*-swarm.jar" --name=bankplus-transactions wildflyswarm-10-centos7~https://github.com/VineetReynolds/bankplus
oc expose service bankplus-transactions
