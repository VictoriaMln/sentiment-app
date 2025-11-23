FROM bellsoft/liberica-runtime-container:jre-17-slim-musl

WORKDIR /app

COPY target/sentiment-app-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
