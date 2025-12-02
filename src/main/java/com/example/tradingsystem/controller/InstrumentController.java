package com.example.tradingsystem.controller;


import com.example.tradingsystem.dto.InstrumentDto;
import com.example.tradingsystem.dto.InstrumentPriceDto;
import com.example.tradingsystem.dto.errors.ErrorResponseDto;
import com.example.tradingsystem.service.IInstrumentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Instruments", description = "Getting instruments from external system - GPW")
@RestController
@RequestMapping(path = "/api/instruments", produces = {MediaType.APPLICATION_JSON_VALUE})
@AllArgsConstructor
public class InstrumentController {
    private IInstrumentService iInstrumentService;

    @Operation(summary = "Fetch Instruments REST API", description = "REST API to fetch instruments from GPW")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "HTTP Status OK"),
            @ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error", content =
            @Content(schema = @Schema(implementation = ErrorResponseDto.class)))})
    @GetMapping()
    public ResponseEntity<List<InstrumentDto>> fetchAllInstruments() {
        List<InstrumentDto> allInstrumentsList = iInstrumentService.fetchAllInstruments();
        return ResponseEntity.status(HttpStatus.OK).body(allInstrumentsList);
    }

    @Operation(summary = "Fetch Instrument Prices REST API", description = "REST API to fetch instrument prices from GPW")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "HTTP Status OK"),
            @ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error", content =
            @Content(schema = @Schema(implementation = ErrorResponseDto.class)))})
    @GetMapping("/prices/current")
    public ResponseEntity<List<InstrumentPriceDto>> fetchAllInstrumentsPrices() {
        List<InstrumentPriceDto> allInstrumentsPrices = iInstrumentService.fetchAllInstrumentsPrices();
        return ResponseEntity.status(HttpStatus.OK).body(allInstrumentsPrices);
    }

    @Operation(summary = "Fetch Instrument by Isin number REST API", description = "REST API to fetch instrument from GPW based on isin number")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "HTTP Status OK"),
            @ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error", content =
            @Content(schema = @Schema(implementation = ErrorResponseDto.class)))})
    @GetMapping("/{isin}")
    public ResponseEntity<InstrumentDto> fetchInstrumentByIsin(@PathVariable("isin") String isin) {
        InstrumentDto instrument = iInstrumentService.fetchInstrumentByIsin(isin);
        return ResponseEntity.status(HttpStatus.OK).body(instrument);
    }

    @Operation(summary = "Fetch Instrument Price by Isin number REST API", description = "REST API to fetch instrument price from GPW based on isin number")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "HTTP Status OK"),
            @ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error", content =
            @Content(schema = @Schema(implementation = ErrorResponseDto.class)))})
    @GetMapping("/prices/current/{isin}")
    public ResponseEntity<InstrumentPriceDto> fetchInstrumentPriceByIsin(@PathVariable("isin") String isin) {
        InstrumentPriceDto instrumentPrice = iInstrumentService.fetchInstrumentPriceByIsin(isin);
        return ResponseEntity.status(HttpStatus.OK).body(instrumentPrice);
    }

}
