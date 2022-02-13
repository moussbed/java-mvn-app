#!/usr/bin/env bash

export TAG=$1
# USE This environment variables we use private docker repository
#export DOCKER_USER=$2
#export DOCKER_PWD=$3
#It is useful when we want pull image from private repository
#echo $DOCKER_PWD | docker login -u $DOCKER_USER --password-stdin
docker-compose -f docker-compose.yaml up --detach
echo "success"
