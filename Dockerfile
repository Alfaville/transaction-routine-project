FROM gradle:jdk11 as gradleimage
COPY . /home/gradle/source
WORKDIR /home/gradle/source
RUN gradle clean
RUN gradle build

FROM adoptopenjdk/openjdk11-openj9:jdk-11.0.10_9_openj9-0.24.0-alpine
LABEL maintainer="Felipe Alfaville"
LABEL version="0.2.0"

ARG JAR_FILE=/home/gradle/source/build/libs/*.jar

WORKDIR /app
RUN mkdir /opt/app
COPY --from=gradleimage ${JAR_FILE} /opt/app/transaction-routine.jar

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=local", "/opt/app/transaction-routine.jar"]