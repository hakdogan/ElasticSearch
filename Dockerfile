FROM maven:3-alpine

COPY pom.xml ES/

COPY src/ ES/src/

WORKDIR ES/

RUN mvn clean install

EXPOSE 8080

CMD ["mvn","jetty:run"]