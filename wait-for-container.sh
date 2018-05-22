#!/usr/bin/env bash
while : ; do
    sleep 1;
    server="-X GET eleasticserver:9200"
    curl -f $server && break || echo "Elasticsearch server isn't responding!";
done;

java -Dnetworkaddress.cache.ttl=60 -jar -Dspring.profiles.active=docker /ElasticSearch/target/ElasticSearch.jar