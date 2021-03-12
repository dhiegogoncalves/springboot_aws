package com.project.aws_project01.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UrlResponse {

    private String url;
    private long expirationTime;
}
