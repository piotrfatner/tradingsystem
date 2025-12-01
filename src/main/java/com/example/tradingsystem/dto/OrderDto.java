package com.example.tradingsystem.dto;

import com.example.tradingsystem.enumes.OrderStatus;
import com.example.tradingsystem.enumes.OrderType;
import com.example.tradingsystem.enumes.TransactionSide;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
public class OrderDto {
    private Long orderId;

    private Long externalOrderId;

    private Long accountId;

    @NotBlank
    @Size(min = 12, max = 12)
    private String isin;

    @NotNull
    private TransactionSide side;

    @NotBlank
    @Size(min = 3, max = 3)
    private String tradeCurrency;

    @NotNull
    @Positive
    private Integer quantity;

    private Instant expiresAt;

    @NotNull
    private OrderType orderType;

    @Positive
    private BigDecimal limitPrice;

    private OrderStatus status;

    private Instant registrationTime;
    // After execution:
    private Instant executedTime;
    private BigDecimal executionPrice;
    private BigDecimal commission;
}
