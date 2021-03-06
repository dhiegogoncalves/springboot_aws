package com.myorg;

import software.amazon.awscdk.core.App;

import org.apache.log4j.BasicConfigurator;

public class AwsCdkApp {
    public static void main(final String[] args) {
        BasicConfigurator.configure();

        App app = new App();

        VpcStack vpcStack = new VpcStack(app, "Vpc");

        ClusterStack clusterStack = new ClusterStack(app, "Cluster", vpcStack.getVpc());
        clusterStack.addDependency(vpcStack);

        RdsStack rdsStack = new RdsStack(app, "Rds", vpcStack.getVpc());
        rdsStack.addDependency(vpcStack);

        SnsStack snsStack = new SnsStack(app, "Sns");

        SqsStack sqsStack = new SqsStack(app, "Sqs", snsStack.getProducEventsTopic());
        sqsStack.addDependency(snsStack);

        DynamoDBStack dynamoDBStack = new DynamoDBStack(app, "DynamoDB");

        InvoiceAppStack invoiceAppStack = new InvoiceAppStack(app, "InvoiceApp");

        Service01Stack service01Stack = new Service01Stack(app, "Service01", clusterStack.getCluster(),
                snsStack.getProducEventsTopic(), invoiceAppStack.getBucket(), invoiceAppStack.getS3InvoiceQueue());
        service01Stack.addDependency(clusterStack);
        service01Stack.addDependency(rdsStack);
        service01Stack.addDependency(snsStack);
        service01Stack.addDependency(invoiceAppStack);

        Service02Stack service02Stack = new Service02Stack(app, "Service02", clusterStack.getCluster(),
                sqsStack.getProductEventsQueue(), dynamoDBStack.getProductEventsDynamoDB());
        service02Stack.addDependency(clusterStack);
        service02Stack.addDependency(sqsStack);
        service02Stack.addDependency(dynamoDBStack);

        app.synth();
    }
}
