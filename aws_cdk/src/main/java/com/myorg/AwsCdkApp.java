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

        Service01Stack service01Stack = new Service01Stack(app, "Service01", clusterStack.getCluster(),
                snsStack.getProducEventsTopic());
        service01Stack.addDependency(clusterStack);
        service01Stack.addDependency(rdsStack);
        service01Stack.addDependency(snsStack);

        Service02Stack service02Stack = new Service02Stack(app, "Service02", clusterStack.getCluster(),
                sqsStack.getProductEventsQueue());
        service02Stack.addDependency(clusterStack);
        service02Stack.addDependency(sqsStack);

        app.synth();
    }
}
