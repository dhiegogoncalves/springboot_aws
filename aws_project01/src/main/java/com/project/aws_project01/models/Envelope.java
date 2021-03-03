package com.project.aws_project01.models;

import com.project.aws_project01.enums.EventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Envelope {
    private EventType eventType;
    private String data;
}
