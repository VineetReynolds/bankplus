FROM java:openjdk-8-jdk

ADD target/bankplus-messaging-swarm.jar /opt/bankplus-messaging-swarm.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/opt/bankplus-messaging-swarm.jar"]