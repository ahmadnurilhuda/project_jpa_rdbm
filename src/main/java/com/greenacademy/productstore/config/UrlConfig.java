package com.greenacademy.productstore.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UrlConfig {
    @Value("${app.base-url}")
    private String baseUrl = "http://localhost:8080";

    public String getBaseUrl() {
        return baseUrl;
    } 
}
