[![Build Status](https://travis-ci.org/hakdogan/ElasticSearch.svg?branch=master)](https://travis-ci.org/hakdogan/ElasticSearch)
!["Docker Pulls](https://img.shields.io/docker/pulls/hakdogan/elasticsearch.svg)
[![Analytics](https://ga-beacon.appspot.com/UA-110069051-1/ElasticSearch/readme)](https://github.com/igrigorik/ga-beacon)

Illustration and demonstration use of ElasticSearch
===================================================

This repository illustrates and demonstrates the use of ElasticSearch Java API with most up to date version of ElasticSearch. If you want to see the sample of the old version, please visit the [oldVersion](https://github.com/hakdogan/ElasticSearch/tree/oldVersion) branch.

## How to run?
```
mvn spring-boot:run
```

## How to run with Docker?
```
docker run -d --name elasticsearch -p 8080:8080 hakdogan/elasticsearch:newVersion
```

![](image/image.gif)