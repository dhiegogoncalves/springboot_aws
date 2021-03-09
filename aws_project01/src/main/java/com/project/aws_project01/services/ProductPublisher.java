package com.project.aws_project01.services;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.PublishResult;
import com.amazonaws.services.sns.model.Topic;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.aws_project01.enums.EventType;
import com.project.aws_project01.models.Envelope;
import com.project.aws_project01.models.Product;
import com.project.aws_project01.models.ProductEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductPublisher {

    private final AmazonSNS snsClient;

    @Qualifier("productEventsTopic")
    private final Topic productEventsTopic;

    private final ObjectMapper objectMapper;

    public void publishProductEvent(Product product, EventType eventType, String username) {
        ProductEvent productEvent = ProductEvent.builder().productId(product.getId()).code(product.getCode())
                .username(username).build();

        Envelope envelope = Envelope.builder().eventType(eventType).build();

        try {
            envelope.setData(objectMapper.writeValueAsString(productEvent));

            PublishResult publishResult = snsClient.publish(productEventsTopic.getTopicArn(),
                    objectMapper.writeValueAsString(envelope));

            log.info("Product event set - Event: {} - ProductId: {} - MessageId: {}", envelope.getEventType(),
                    productEvent.getProductId(), publishResult.getMessageId());

        } catch (Exception ex) {
            log.error("Failed to create product event message");
        }
    }
}
