FROM maven:3-alpine

LABEL maintainer = "Hüseyin Akdoğan <huseyin.akdogan@kodcu.com>"

VOLUME /var/log

COPY pom.xml ElasticSearch/

COPY src/ ElasticSearch/src/

WORKDIR ElasticSearch/

RUN mvn clean install -Dmaven.test.skip=true

EXPOSE 8080

CMD ["sh","-c", "java -Dnetworkaddress.cache.ttl=60 -jar /ElasticSearch/target/ElasticSearch.jar"]