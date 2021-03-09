package com.project.aws_project02.services;

import java.io.IOException;

import javax.jms.JMSException;
import javax.jms.TextMessage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.aws_project02.models.Envelope;
import com.project.aws_project02.models.ProductEvent;
import com.project.aws_project02.models.SnsMessage;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductEventConsumer {

    private final ObjectMapper objectMapper;

    @JmsListener(destination = "${aws.sqs.queue.product.events.name}")
    public void receiveProductEvent(TextMessage textMessage) throws JMSException, IOException {
        SnsMessage snsMessage = objectMapper.readValue(textMessage.getText(), SnsMessage.class);

        Envelope envelope = objectMapper.readValue(snsMessage.getMessage(), Envelope.class);

        ProductEvent productEvent = objectMapper.readValue(envelope.getData(), ProductEvent.class);

        log.info("Product event received - Event: {} - ProductId: {} - MessageId: {}", envelope.getEventType(),
                productEvent.getProductId(), snsMessage.getMessageId());
    }

}
