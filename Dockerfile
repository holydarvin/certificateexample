FROM openjdk:17

RUN microdnf install findutils

ENV APP_HOME=/usr/app/
WORKDIR $APP_HOME

COPY generate-certificate.sh /

RUN chmod +x /generate-certificate.sh && /generate-certificate.sh

COPY build.gradle settings.gradle gradlew $APP_HOME
COPY gradle $APP_HOME/gradle
COPY src $APP_HOME/src

RUN ./gradlew clean build


CMD ["java", "-jar", "./build/libs/certificate-example-0.0.1-SNAPSHOT.jar"]