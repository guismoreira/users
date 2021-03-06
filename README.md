# Orion Users Service

Orion Users Service microservice

## Run with Docker Compose

The easer way to install and execute the Orion Users Service is to use docker-composer. Once Docker is installed, you can run the bellow commands:

    mvn clean package
    docker-compose up -d

Note: Default database root and password is: orion-users-service

## Docker image

To create a Docker image for Orion Users Service:

    mvn clean package

    docker build -t orion-users-image .

    docker run -d --name orion-users-service -p 9080:9080 -p 9443:9443 orion-users-image

### Orion User dev mode

During development, you can use Liberty's development mode (dev mode) to code while observing and testing your changes on the fly.

    mvn clear package
    mvn liberty:dev
