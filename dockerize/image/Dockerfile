FROM openjdk:11
MAINTAINER Timofeev Kirill <timofeev.log@narod.ru>
ARG JAR_FILE
WORKDIR /usr/local/web
COPY ${JAR_FILE} app.jar
CMD ["java", "-Xmx1G", "-jar", "app.jar"]