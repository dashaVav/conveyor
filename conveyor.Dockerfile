FROM openjdk:21-slim
LABEL authors="dashavav"
COPY target/conveyor*.jar /conveyor.jar
ENTRYPOINT ["java", "-jar", "/conveyor.jar"]