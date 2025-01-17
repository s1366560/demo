FROM openjdk:17-jdk
WORKDIR /app
EXPOSE 8080
ENV jar calculation-api-1.0.0-SNAPSHOT.jar
COPY target/${jar} /app/

CMD java -jar ${jar}