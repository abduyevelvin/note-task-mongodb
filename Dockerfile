FROM openjdk:17-jdk-slim
ADD target/note-task-mongodb-docker.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]