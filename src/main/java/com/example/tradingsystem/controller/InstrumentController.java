package com.example.tradingsystem.controller;


import com.example.tradingsystem.dto.InstrumentDto;
import com.example.tradingsystem.dto.InstrumentPriceDto;
import com.example.tradingsystem.service.IInstrumentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/instruments", produces = {MediaType.APPLICATION_JSON_VALUE})
@AllArgsConstructor
public class InstrumentController {
    private IInstrumentService iInstrumentService;

    @GetMapping()
    public ResponseEntity<List<InstrumentDto>> fetchAllInstruments() {
        List<InstrumentDto> allInstrumentsList = iInstrumentService.fetchAllInstruments();
        return ResponseEntity.status(HttpStatus.OK).body(allInstrumentsList);
    }

    @GetMapping("/{isin}") // TODO: Check if necessary
    public ResponseEntity<InstrumentDto> fetchInstrumentByIsin(@PathVariable("isin") String isin) {
        InstrumentDto instrument = iInstrumentService.fetchInstrumentByIsin(isin);
        return ResponseEntity.status(HttpStatus.OK).body(instrument);
    }

    @GetMapping("/prices/current/{isin}")
    public ResponseEntity<InstrumentPriceDto> fetchInstrumentPriceByIsin(@PathVariable("isin") String isin) {
        InstrumentPriceDto instrumentPrice = iInstrumentService.fetchInstrumentPriceByIsin(isin);
        return ResponseEntity.status(HttpStatus.OK).body(instrumentPrice);
    }

    @GetMapping("/prices/current")
    public ResponseEntity<List<InstrumentPriceDto>> fetchAllInstrumentsPrices() {
        List<InstrumentPriceDto> allInstrumentsPrices = iInstrumentService.fetchAllInstrumentsPrices();
        return ResponseEntity.status(HttpStatus.OK).body(allInstrumentsPrices);
    }
}
