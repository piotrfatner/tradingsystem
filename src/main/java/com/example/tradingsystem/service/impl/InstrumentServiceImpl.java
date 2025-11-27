package com.example.tradingsystem.service.impl;

import com.example.tradingsystem.dto.InstrumentDto;
import com.example.tradingsystem.dto.InstrumentPriceDto;
import com.example.tradingsystem.service.IInstrumentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class InstrumentServiceImpl implements IInstrumentService {
    @Override
    public List<InstrumentDto> fetchAllInstruments() {
        return List.of();
    }

    @Override
    public InstrumentDto fetchInstrumentByIsin(String isin) {
        return null;
    }

    @Override
    public List<InstrumentPriceDto> fetchAllInstrumentsPrices() {
        return List.of();
    }

    @Override
    public InstrumentPriceDto fetchInstrumentPriceByIsin(String isin) {
        return null;
    }
}
