#!/bin/bash

ENVIRONMENT=""

deployment_dev()
{
  ./gradlew clean
  ./gradlew build
  ./gradlew bootRun --args='--spring.profiles.active=dev'
}

deployment_prod()
{
  ./gradlew bootBuildImage
  docker-compose down
  docker-compose pull
  docker-compose up -d
  docker-compose ps
}

environment_request()
{
  echo "In which environment do you want to run? "
  echo "DEV or PROD"
  read ENVIRONMENT
}

while [ "$ENVIRONMENT" == "" ]
do
  environment_request
  if [ "$ENVIRONMENT" == "DEV" ]; then
    deployment_dev
  elif [ "$ENVIRONMENT" == "PROD" ]; then
    deployment_prod
  else
    echo "Invalid environment. Try again!"
    ENVIRONMENT=""
  fi
done

echo "Running in the $ENVIRONMENT environment"
