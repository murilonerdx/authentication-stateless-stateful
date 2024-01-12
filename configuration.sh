#!/bin/bash

# Parar containers
docker ps -aq
docker container prune -f

docker container run --name stateless-auth-db -p 5432:5432 -e POSTGRES_DB=auth-db -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres postgres:latest
docker container run --name stateful-auth-db -p 5433:5432 -e POSTGRES_DB=auth-db -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres postgres:latest
docker container run --name token-redis -p 6379 -d redis:latest
docker logs --follow token-redis
docker exec -it token-redis redis-cli