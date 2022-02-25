# Local Setup scripts/files

Running Principal Web+Backend+MySql:

> docker-compose down && docker-compose build --pull && docker-compose up -d


Pulls:

docker pull 340356991954.dkr.ecr.eu-west-1.amazonaws.com/typ/principal-client:latest-local



docker pull 340356991954.dkr.ecr.eu-west-1.amazonaws.com/typ/principals-mng:latest

Independent Runs:

* Backend with env file:
> docker run --name principals-mng-local --env-file principal-mng.env -d -p 5001:80 340356991954.dkr.ecr.eu-west-1.amazonaws.com/typ/principals-mng

* Fronend pre-build to run DEV-SERVER:
> docker run --name principal-client-local --env-file principal-client.env -d -p 1338:80 340356991954.dkr.ecr.eu-west-1.amazonaws.com/typ/principal-client

* Fronend pre-build to run LOCALLY:
> docker run --name principal-client-local -d -p 1338:80 340356991954.dkr.ecr.eu-west-1.amazonaws.com/typ/principal-client:latest-local