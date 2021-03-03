package com.myorg;

import software.amazon.awscdk.core.Construct;
import software.amazon.awscdk.core.Stack;
import software.amazon.awscdk.core.StackProps;
import software.amazon.awscdk.services.events.targets.SnsTopic;
import software.amazon.awscdk.services.sns.Topic;
import software.amazon.awscdk.services.sns.subscriptions.EmailSubscription;

public class SnsStack extends Stack {

    private final SnsTopic producEventsTopic;

    public SnsStack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public SnsStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);

        producEventsTopic = SnsTopic.Builder
                .create(Topic.Builder.create(this, "ProductEventsTopic").topicName("product-events").build()).build();

        producEventsTopic.getTopic()
                .addSubscription(EmailSubscription.Builder.create("dhhiego@gmail.com").json(true).build());

    }

    public SnsTopic getProducEventsTopic() {
        return producEventsTopic;
    }
}
