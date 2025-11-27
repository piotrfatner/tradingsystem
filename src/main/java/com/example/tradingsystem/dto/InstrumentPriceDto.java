package com.example.tradingsystem.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class InstrumentPriceDto {
    private String isin;
    private BigDecimal price;
}
