# docker build . -t cartanatal-docker
# docker run -p 8080:8080 --name cartanatal cartanatal-docker

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
RUN apt-get update \
 && apt-get install -y --no-install-recommends \
      fontconfig \
 && rm -rf /var/lib/apt/lists/*
RUN mkdir -p /usr/local/cartanatal/fonts
COPY --from=build /home/app/target/*.jar /usr/local/lib/cartanatal.jar
COPY --from=build /home/app/src/main/resources/book/*.json /usr/local/cartanatal/book/
COPY --from=build /home/app/src/main/resources/ephe/*.* /usr/local/cartanatal/ephe/
COPY --from=build /home/app/src/main/resources/fonts/*.* /usr/local/cartanatal/fonts/
RUN ls -l /usr/local/cartanatal/fonts
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/local/lib/cartanatal.jar"]


# FROM adoptopenjdk/openjdk11-openj9:jdk-11.0.1.13-alpine-slim
# WORKDIR /app
# COPY . ./
# RUN mvn clean package
# COPY target/*.jar cartanatal.jar
# EXPOSE 8080
# CMD java  -XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap -Dcom.sun.management.jmxremote -noverify ${JAVA_OPTS} -jar cartanatal.jar