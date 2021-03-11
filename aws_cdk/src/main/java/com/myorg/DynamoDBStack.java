package com.myorg;

import software.amazon.awscdk.core.Construct;
import software.amazon.awscdk.core.Duration;
import software.amazon.awscdk.core.RemovalPolicy;
import software.amazon.awscdk.core.Stack;
import software.amazon.awscdk.core.StackProps;
import software.amazon.awscdk.services.dynamodb.Attribute;
import software.amazon.awscdk.services.dynamodb.AttributeType;
import software.amazon.awscdk.services.dynamodb.BillingMode;
import software.amazon.awscdk.services.dynamodb.EnableScalingProps;
import software.amazon.awscdk.services.dynamodb.Table;
import software.amazon.awscdk.services.dynamodb.UtilizationScalingProps;

public class DynamoDBStack extends Stack {

    private Table productEventsDynamoDB;

    public DynamoDBStack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public DynamoDBStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);

        productEventsDynamoDB = Table.Builder.create(this, "ProductEventsDb").tableName("product-events")
                .billingMode(BillingMode.PROVISIONED).readCapacity(1).writeCapacity(1)
                .partitionKey(Attribute.builder().name("pk").type(AttributeType.STRING).build())
                .sortKey(Attribute.builder().name("sk").type(AttributeType.STRING).build()).timeToLiveAttribute("ttl")
                .removalPolicy(RemovalPolicy.DESTROY).build();

        productEventsDynamoDB.autoScaleReadCapacity(EnableScalingProps.builder().minCapacity(1).maxCapacity(4).build())
                .scaleOnUtilization(UtilizationScalingProps.builder().targetUtilizationPercent(50)
                        .scaleInCooldown(Duration.seconds(30)).scaleOutCooldown(Duration.seconds(30)).build());

        productEventsDynamoDB.autoScaleWriteCapacity(EnableScalingProps.builder().minCapacity(1).maxCapacity(4).build())
                .scaleOnUtilization(UtilizationScalingProps.builder().targetUtilizationPercent(50)
                        .scaleInCooldown(Duration.seconds(30)).scaleOutCooldown(Duration.seconds(30)).build());
    }

    public Table getProductEventsDynamoDB() {
        return productEventsDynamoDB;
    }
}
