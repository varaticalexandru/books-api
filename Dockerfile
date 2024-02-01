FROM openjdk:17
MAINTAINER alexv.com
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app/jar"]