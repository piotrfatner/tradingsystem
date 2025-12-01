package com.example.tradingsystem.dto;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "account")
public record AccountInfo(String username, Long accountId) {
}
