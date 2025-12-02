package com.example.tradingsystem.dto;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "restclient")
public record RestClientConfigSettings(Long connectTimeoutMs, Long readTimeoutMs, Integer maxConnTotal,
                                       Integer maxConnPerRoute) {
}
