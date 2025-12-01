package com.example.tradingsystem.dto;

import com.example.tradingsystem.enumes.TransactionSide;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
public class OrderResponseDetailsDto {
    private Long orderId;
    private String status;
    private String isin;
    private TransactionSide side;
    private String tradeCurrency;
    private Integer quantity;
    private BigDecimal executionPrice;
    private Instant registrationTime;
    private Instant executedTime;
}
