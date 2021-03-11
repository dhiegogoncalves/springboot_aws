# Welcome to your CDK Java project!

This is a blank project for Java development with CDK.

The `cdk.json` file tells the CDK Toolkit how to execute your app.

It is a [Maven](https://maven.apache.org/) based project, so you can open this project with any Maven compatible Java IDE to build and run tests.

## Useful commands

-   `mvn package` compile and run tests
-   `cdk ls` list all stacks in the app
-   `cdk synth` emits the synthesized CloudFormation template
-   `cdk deploy` deploy this stack to your default AWS account/region
-   `cdk diff` compare deployed stack with current state
-   `cdk docs` open CDK documentation

Enjoy!

Steps --

AWS CLI -> aws configure --profile user
install cdk -> npm install -g aws-cdk
check version -> cdk --version
create project -> cdk init app --language java

cdk deploy --parameters Rds:databasePassword=SE8R3t_k3y Vpc Cluster Rds Sns Sqs DynamoDB Service01 Service02 --profile default

cdk destroy Vpc Cluster Rds Sns Sqs DynamoDB Service01 Service02 --profile default
