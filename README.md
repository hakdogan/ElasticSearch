[![Build Status](https://travis-ci.org/hakdogan/ElasticSearch.svg?branch=master)](https://travis-ci.org/hakdogan/ElasticSearch)
!["Docker Pulls](https://img.shields.io/docker/pulls/hakdogan/elasticsearch.svg)
[![Coverage Status](https://coveralls.io/repos/github/hakdogan/ElasticSearch/badge.svg?branch=master)](https://coveralls.io/github/hakdogan/ElasticSearch?branch=master)
[![Analytics](https://ga-beacon.appspot.com/UA-110069051-1/ElasticSearch/readme)](https://github.com/igrigorik/ga-beacon)

Illustration and demonstration use of ElasticSearch
===================================================

This repository illustrates and demonstrates the use of ElasticSearch Java API with most up to date version of ElasticSearch which provides _Java High Level REST Client_. If you want to see the sample of the old version, please visit the [oldVersion](https://github.com/hakdogan/ElasticSearch/tree/oldVersion) branch.

## How to Use Java High Level REST Client in the backend?
The client added in version 6.0.0-beta1 and it works on top of the Java low level rest client.

### Initialization
```java
RetHighLevelClient(RestClient.builder(new HttpHost(props.getRestClient().getHostname(),
                props.getRestClient().getPort(), props.getRestClient().getScheme())));
```


### Creating an index
```java
IndexRequest request = new IndexRequest(props.getIndex().getName(), props.getIndex().getType());
request.source(gson.toJson(document), XContentType.JSON);
IndexResponse response = client.index(request);
```

### Using SearchSourceBuilder and showing search results
```java
sourceBuilder.query(builder);
SearchRequest searchRequest = getSearchRequest();

SearchResponse searchResponse = client.search(searchRequest);
SearchHits hits = searchResponse.getHits();
SearchHit[] searchHits = hits.getHits();
for (SearchHit hit : searchHits) {
    Document doc = gson.fromJson(hit.getSourceAsString(), Document.class);
    doc.setId(hit.getId());
    result.add(doc);
}
```

### Using wildcard query
```java
QueryBuilders.wildcardQuery("_all", "*" + query.toLowerCase() + "*")
```

### Deleting a document
```
DeleteRequest deleteRequest = new DeleteRequest(props.getIndex().getName(), props.getIndex().getType(), id);
```

## How to run?
```
mvn spring-boot:run
```

## How to run with Docker?
```
docker run -d --name elasticsearch -p 8080:8080 hakdogan/elasticsearch:newVersion
```

![](image/image.gif)