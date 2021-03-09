package com.myorg;

import java.util.HashMap;
import java.util.Map;

import software.amazon.awscdk.core.Construct;
import software.amazon.awscdk.core.Duration;
import software.amazon.awscdk.core.RemovalPolicy;
import software.amazon.awscdk.core.Stack;
import software.amazon.awscdk.core.StackProps;
import software.amazon.awscdk.services.applicationautoscaling.EnableScalingProps;
import software.amazon.awscdk.services.ecs.AwsLogDriverProps;
import software.amazon.awscdk.services.ecs.Cluster;
import software.amazon.awscdk.services.ecs.ContainerImage;
import software.amazon.awscdk.services.ecs.CpuUtilizationScalingProps;
import software.amazon.awscdk.services.ecs.LogDriver;
import software.amazon.awscdk.services.ecs.ScalableTaskCount;
import software.amazon.awscdk.services.ecs.patterns.ApplicationLoadBalancedFargateService;
import software.amazon.awscdk.services.ecs.patterns.ApplicationLoadBalancedTaskImageOptions;
import software.amazon.awscdk.services.elasticloadbalancingv2.HealthCheck;
import software.amazon.awscdk.services.logs.LogGroup;
import software.amazon.awscdk.services.sqs.Queue;

public class Service02Stack extends Stack {
    public Service02Stack(final Construct scope, final String id, Cluster cluster, Queue productEventsQueue) {
        this(scope, id, null, cluster, productEventsQueue);
    }

    public Service02Stack(final Construct scope, final String id, final StackProps props, Cluster cluster,
            Queue productEventsQueue) {
        super(scope, id, props);

        Map<String, String> envVariables = new HashMap<>();
        envVariables.put("AWS_REGION", "us-east-1");
        envVariables.put("AWS_SQS_QUEUE_PRODUCT_EVENTS_NAME", productEventsQueue.getQueueName());

        ApplicationLoadBalancedFargateService service02 = ApplicationLoadBalancedFargateService.Builder
                .create(this, "ALB02").serviceName("service-02").cluster(cluster).cpu(512).memoryLimitMiB(1024)
                .desiredCount(2).listenerPort(9090)
                .taskImageOptions(ApplicationLoadBalancedTaskImageOptions.builder().containerName("aws_project02")
                        .image(ContainerImage.fromRegistry("dhhiego/aws_project02:1.1.2")).containerPort(9090)
                        .logDriver(LogDriver.awsLogs(AwsLogDriverProps.builder()
                                .logGroup(LogGroup.Builder.create(this, "Service02LogGroup").logGroupName("Service02")
                                        .removalPolicy(RemovalPolicy.DESTROY).build())
                                .streamPrefix("Service02").build()))
                        .environment(envVariables).build())
                .publicLoadBalancer(true).build();

        service02.getTargetGroup().configureHealthCheck(
                new HealthCheck.Builder().path("/actuator/health").port("9090").healthyHttpCodes("200").build());

        ScalableTaskCount scalableTaskCount = service02.getService()
                .autoScaleTaskCount(EnableScalingProps.builder().minCapacity(2).maxCapacity(4).build());

        scalableTaskCount.scaleOnCpuUtilization("Service02AutoScaling",
                CpuUtilizationScalingProps.builder().targetUtilizationPercent(50).scaleInCooldown(Duration.seconds(60))
                        .scaleOutCooldown(Duration.seconds(60)).build());

        productEventsQueue.grantConsumeMessages(service02.getTaskDefinition().getTaskRole());
    }
}
