FROM maven:3-alpine

LABEL maintainer = "Hüseyin Akdoğan <huseyin.akdogan@kodcu.com>"

VOLUME /var/log

COPY pom.xml ElasticSearch/

COPY src/ ElasticSearch/src/

COPY wait-for-container.sh /wait-for-container.sh

WORKDIR ElasticSearch/

RUN mvn clean install -Dmaven.test.skip=true

EXPOSE 8080

ENTRYPOINT ["sh", "/wait-for-container.sh"]