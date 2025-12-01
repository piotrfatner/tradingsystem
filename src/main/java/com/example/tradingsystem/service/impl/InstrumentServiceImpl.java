package com.example.tradingsystem.service.impl;

import com.example.tradingsystem.client.GpwClient;
import com.example.tradingsystem.dto.*;
import com.example.tradingsystem.service.IInstrumentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InstrumentServiceImpl implements IInstrumentService {
    private final GpwClient gpwClient;

    public InstrumentServiceImpl(GpwClient gpwClient) {
        this.gpwClient = gpwClient;
    }

    @Override
    public List<InstrumentDto> fetchAllInstruments() {
        return gpwClient.fetchAllInstrumentsFromGpw();
    }

    @Override
    public List<InstrumentPriceDto> fetchAllInstrumentsPrices() {
        return gpwClient.fetchAllInstrumentsPrices();
    }

    @Override
    public InstrumentDto fetchInstrumentByIsin(String isin) {
        return fetchAllInstruments().stream()
                .filter(i -> i.getIsin().equals(isin))
                .findFirst()
                .orElse(null);
    }

    @Override
    public InstrumentPriceDto fetchInstrumentPriceByIsin(String isin) {
        return null;
    }
}
