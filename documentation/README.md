# Toptal

The toptal platform has been develop with the idea of micro-services. Each identified entity will have it's own services and database to perform all actions related with the entity data.

Access overview/general-user-flow.puml to see a high overview of the full flow of the user in the platform.

# Tech

## Event-Sourcing
We are using an Event-Sourcing architecture for following key points:
- Data become the numer one level citizen, every single action that occurs in the system gets stored in the form of an event
- The performance of the system will keep a good shape in the long term
- The scalability of the project, both vertically and horizontally will be easy to implement
- The project will be stateless, so the Continuous Deployment, Orchestration, and Containerization.
- Events are the final source of truth. Aggregates will be built to always return the most up to date version of an entity.

## CQRS
The structure of the proyect follows a CQRS pattern:
- Reading: controller -> service -> aggregate -> db
- Writing: controller -> service/producer -> repository -> db
- Events: consumer -> handler -> service/repository

## Events

The platform will be (for the MVP) using AWS services to produce/consume events. Going Cloud agnostic is too costly for the beginning of the proyect. We are using SQS queues to move domain events across the platform (fifo queues when processing order matters).

## Local Development environment

In the folder dev-environment you can find a project that allows developers to replicate the entire platform quite easy. It is based on Docker Compose and will run up all services and databases needed to work. It uses Localstack (local AWS) to replicate AWS behaviour so the development process is more aligned with what a Sandobx/QA/PROD environment would be. Read the README of the project for more information.