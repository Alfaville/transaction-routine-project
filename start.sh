#!/bin/bash

ENVIRONMENT=""

deployment_dev()
{
  gradle clean
  gradle build
  gradle bootRun --args='--spring.profiles.active=dev'
}

deployment_local()
{
  echo "Stopping the containers"
  docker stop $(docker container ls -q)
  echo "Running PostgreSQL container"
  docker run -e POSTGRES_PASSWORD=postgres -e POSTGRES_USER=postgres -e POSTGRES_DB=accountdb -d -p 5432:5432 postgres:12
  docker build -t io_pismo/transaction-routine .
  docker run -p 9000:9000 io_pismo/transaction-routine
  echo "Project running on 9000 port"
}

deployment_prod()
{
  gradle bootBuildImage
  docker-compose down
  docker-compose pull
  docker-compose up -d
  docker-compose ps
}

environment_request()
{
  echo "In which environment do you want to run? "
  echo "DEV | LOCAL | PROD"
  read ENVIRONMENT
}

while [ "$ENVIRONMENT" == "" ]
do
  environment_request
  if [ "$ENVIRONMENT" == "DEV" ]; then
    deployment_dev
  elif [ "$ENVIRONMENT" == "LOCAL" ]; then
    deployment_local
  elif [ "$ENVIRONMENT" == "PROD" ]; then
    deployment_prod
  else
    echo "Invalid environment. Try again!"
    ENVIRONMENT=""
  fi
done

echo "Running in the $ENVIRONMENT environment"
