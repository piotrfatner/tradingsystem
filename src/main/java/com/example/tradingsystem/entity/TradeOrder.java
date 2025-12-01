package com.example.tradingsystem.entity;

import com.example.tradingsystem.enumes.OrderStatus;
import com.example.tradingsystem.enumes.OrderType;
import com.example.tradingsystem.enumes.TransactionSide;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "trade_orders")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TradeOrder extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="order_id")
    private Long orderId;

    @Column(name = "external_order_id")
    private Long externalOrderId;

    @Column(name = "account_id")
    private Long accountId;

    @Column(name="isin")
    private String isin;

    @Column(name = "side")
    @Enumerated(EnumType.STRING)
    private TransactionSide side;

    @Column(name = "trade_currency")
    private String tradeCurrency;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "expires_at")
    private Instant expiresAt;

    @Column(name = "order_type")
    @Enumerated(EnumType.STRING)
    private OrderType orderType;

    @Column(name = "limit_price")
    private BigDecimal limitPrice;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(name = "registration_time")
    private Instant registrationTime;

    @Column(name = "executed_time")
    private Instant executedTime;

    @Column(name = "execution_price")
    private BigDecimal executionPrice;

    @Column(name = "mic")
    private String mic;

    @Column(name = "commission")
    private BigDecimal commission;
}
