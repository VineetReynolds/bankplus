FROM java:openjdk-8-jdk

ADD target/bankplus-accounting-swarm.jar /opt/bankplus-accounting-swarm.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/opt/bankplus-accounting-swarm.jar"]