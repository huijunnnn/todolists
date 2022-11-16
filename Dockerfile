FROM openjdk:18
VOLUME /tmp
ADD ./target/*.jar ./app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom","-jar","app.jar"]
