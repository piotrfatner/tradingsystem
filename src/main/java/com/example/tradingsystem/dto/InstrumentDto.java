package com.example.tradingsystem.dto;

import lombok.Data;

@Data
public class InstrumentDto {
    private String name;
    private String isin;
    private String ticker;
    private String tradeCurrency;
    private String mic;
}
