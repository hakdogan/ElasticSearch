#!/usr/bin/env bash
mvn clean install
docker-compose -f docker-compose.yml up --build