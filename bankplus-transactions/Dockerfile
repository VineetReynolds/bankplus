FROM java:openjdk-8-jdk

ADD target/bankplus-transactions-swarm.jar /opt/bankplus-transactions-swarm.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/opt/bankplus-transactions-swarm.jar"]