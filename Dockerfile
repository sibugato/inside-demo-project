FROM amazoncorretto:17.0.3
LABEL maintainer="sibgatullin_vladislav"
ADD target/inside-demo-project-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar","/app.jar"]
