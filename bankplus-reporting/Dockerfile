FROM java:openjdk-8-jdk

ADD target/bankplus-reporting-swarm.jar /opt/bankplus-reporting-swarm.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/opt/bankplus-reporting-swarm.jar"]