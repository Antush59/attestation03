FROM bellsoft/liberica-openjdk-debian:17.0.12

EXPOSE 8080
ENV APP_HOME /app
ENV JAVA_OPTS="-Dspring.profiles.active=prom --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.lang.reflect=ALL-UNNAMED"

RUN mkdir $APP_HOME

COPY /target/*.jar $APP_HOME/app.jar
WORKDIR $APP_HOME

ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -jar app.jar"]