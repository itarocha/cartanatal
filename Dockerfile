# docker build . -t cartanatal-doccker
# docker run -p 8080:8080 -n cartanatal cartanatal-docker

#
# Build stage
#
FROM maven:3.6.0-jdk-11-slim AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package

#
# Package stage
#
FROM openjdk:11-jre-slim
COPY --from=build /home/app/target/*.jar /usr/local/lib/cartanatal.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/local/lib/cartanatal.jar"]





# FROM adoptopenjdk/openjdk11-openj9:jdk-11.0.1.13-alpine-slim
# WORKDIR /app
# COPY . ./
# RUN mvn clean package
# COPY target/*.jar cartanatal.jar
# EXPOSE 8080
# CMD java  -XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap -Dcom.sun.management.jmxremote -noverify ${JAVA_OPTS} -jar cartanatal.jar