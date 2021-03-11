# SPRING BOOT AWS

### Configuration

## Initialize

(localstack / mariadb)

- docker-compose up -d
- aws dynamodb create-table --cli-input-json file://aws_project02/aws/dynamodb_table.json --endpoint-url=http://localhost:4566
- aws --endpoint-url=http://localhost:4566 sns create-topic --name product-events
- aws --endpoint-url=http://localhost:4566 sqs create-queue --queue-name product-events
- aws --endpoint-url=http://localhost:4566 sns subscribe --topic-arn arn:aws:sns:us-east-1:000000000000:product-events --protocol sqs --notification-endpoint arn:aws:sns:us-east-1:000000000000:product-events

## Verify

- aws --endpoint-url=http://localhost:4566 dynamodb list-tables
- aws --endpoint-url=http://localhost:4566 sns list-topics
- aws --endpoint-url=http://localhost:4566 sqs list-queues
- aws --endpoint-url=http://localhost:4566 sns list-subscriptions
