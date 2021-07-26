FROM openjdk:8-jdk-alpine
COPY target/spring-boot-rest-service-0.0.1-SNAPSHOT.jar library-service.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","library-service.jar"]
