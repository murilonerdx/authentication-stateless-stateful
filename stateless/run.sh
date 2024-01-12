#!/bin/bash

docker image build -t stateless-auth-api .
docker container run --name stateless-auth-api -p 8080:8080 --network auth_auth -e "DB_HOST=stateless-auth-db" -e "DB_PORT=5432" -e "DB_NAME=auth-db" -e "PORT=8080" stateless-auth-api