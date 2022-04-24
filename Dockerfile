FROM openjdk:11
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} mancala-game-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","mancala-game-0.0.1-SNAPSHOT.jar"]