package com.project.aws_project02.dtos;

import com.project.aws_project02.enums.EventType;
import com.project.aws_project02.models.ProductEventLog;

import lombok.Getter;

@Getter
public class ProductEventLogDto {

    private final String code;
    private final EventType eventType;
    private final long productId;
    private final String username;
    private final String messageId;
    private final long timestamp;

    public ProductEventLogDto(ProductEventLog productEventLog) {
        this.code = productEventLog.getPk();
        this.eventType = productEventLog.getEventType();
        this.productId = productEventLog.getProductId();
        this.username = productEventLog.getUsername();
        this.messageId = productEventLog.getMessageId();
        this.timestamp = productEventLog.getTimestamp();
    }
}
