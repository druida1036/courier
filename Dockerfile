FROM amazoncorretto:11-alpine-jdk
MAINTAINER baeldung.com
COPY target/courier-0.0.1-SNAPSHOT.jar courier-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/courier-0.0.1-SNAPSHOT.jar"]