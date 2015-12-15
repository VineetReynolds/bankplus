FROM java:openjdk-8-jdk

ADD target/bankplus-customers-swarm.jar /opt/bankplus-customers-swarm.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/opt/bankplus-customers-swarm.jar"]