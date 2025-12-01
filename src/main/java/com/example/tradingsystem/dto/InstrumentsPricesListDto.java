package com.example.tradingsystem.dto;

import lombok.Data;

import java.util.List;

@Data
public class InstrumentsPricesListDto {
    private List<InstrumentPriceDto> results;
}
