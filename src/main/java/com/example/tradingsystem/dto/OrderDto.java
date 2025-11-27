package com.example.tradingsystem.dto;

import com.example.tradingsystem.enumes.OrderType;
import com.example.tradingsystem.enumes.TransactionSide;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
public class OrderDto {
    private String isin;
    private TransactionSide side;
    private OrderType orderType;
    private Integer quantity;
    private BigDecimal limitPrice;
    private String tradeCurrency;
    private Instant expiresAt;
    //private String status;
    //private Instant registrationTime;
    //private Instant executedTime;
    //private BigDecimal executionPrice;
    //private BigDecimal commission;
}
