package com.project.aws_project02.models;

import com.project.aws_project02.enums.EventType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Envelope {

    private EventType eventType;
    private String data;
}
