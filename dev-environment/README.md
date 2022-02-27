## local-env

This repository is a quick way of having a development environment ready to work on local.

# Localstack

We are using Localstack to have a local development environment emulating AWS Cloud services. For more information, access https://hub.docker.com/r/localstack/localstack

You need to run aws configure to set the env variables needed to work. You can set any values, SDK when working on local will look for the configuration to have a valid format but it won't try a real authentication.

## SQS

Amazon Simple Queue Service (SQS) is a fully managed message queuing service that enables you to decouple and scale microservices, distributed systems, and serverless applications. We use SQS to have a producer/consumer pattern.

Useful commands when working with SQS *(--endpoint-url only needed if working locally)*:

- `aws --endpoint-url http://localhost:4566 sqs create-queue --queue-name UserSignedUpQueueURL`
- `aws --endpoint-url http://localhost:4566 sqs create-queue --queue-name PlayerBoughtQueueURL.fifo`

- `aws --endpoint-url http://localhost:4566 sqs list-queues`


- `aws --endpoint-url http://localhost:4566 sqs purge-queue --queue-url "http://localhost:4566/000000000000/UserSignedUpQueueURL"`


- `aws --endpoint-url http://localhost:4566 sqs send-message --queue-url "http://localhost:4566/000000000000/UserSignedUpQueueURL"  --message-body '{"id": "123456"}'`