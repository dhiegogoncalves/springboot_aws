package com.myorg;

import software.amazon.awscdk.core.Construct;
import software.amazon.awscdk.core.RemovalPolicy;
import software.amazon.awscdk.core.Stack;
import software.amazon.awscdk.core.StackProps;
import software.amazon.awscdk.services.events.targets.SnsTopic;
import software.amazon.awscdk.services.sns.subscriptions.SqsSubscription;
import software.amazon.awscdk.services.sqs.DeadLetterQueue;
import software.amazon.awscdk.services.sqs.Queue;

public class SqsStack extends Stack {

    private Queue productEventsQueue;

    public Queue getProductEventsQueue() {
        return productEventsQueue;
    }

    public SqsStack(final Construct scope, final String id, SnsTopic producEventsTopic) {
        this(scope, id, null, producEventsTopic);
    }

    public SqsStack(final Construct scope, final String id, final StackProps props, SnsTopic producEventsTopic) {
        super(scope, id, props);

        Queue productEventsDql = Queue.Builder.create(this, "ProductEventsDql").queueName("product-events-dql")
                .removalPolicy(RemovalPolicy.DESTROY).build();

        DeadLetterQueue deadLetterQueue = DeadLetterQueue.builder().queue(productEventsDql).maxReceiveCount(3).build();

        productEventsQueue = Queue.Builder.create(this, "ProductEvents").queueName("product-events")
                .deadLetterQueue(deadLetterQueue).removalPolicy(RemovalPolicy.DESTROY).build();

        SqsSubscription sqsSubscription = SqsSubscription.Builder.create(productEventsQueue).build();
        producEventsTopic.getTopic().addSubscription(sqsSubscription);
    }
}
