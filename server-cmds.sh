#!/bin/bash

export IMAGE=$1
docker-compose -f docker-compose.yaml up --detach
echo "success"
export TEST=testValue
