package com.example.tradingsystem.service;

import com.example.tradingsystem.dto.InstrumentDto;
import com.example.tradingsystem.dto.InstrumentPriceDto;

import java.util.List;

public interface IInstrumentService {
    List<InstrumentDto> fetchAllInstruments();

    InstrumentDto fetchInstrumentByIsin(String isin);

    List<InstrumentPriceDto> fetchAllInstrumentsPrices();

    InstrumentPriceDto fetchInstrumentPriceByIsin(String isin);
}
