package com.example.tradingsystem.service.impl;

import com.example.tradingsystem.dto.InstrumentDto;
import com.example.tradingsystem.dto.InstrumentPriceDto;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock(port = 9000)
@TestPropertySource(properties = {
        "gpw.base-url=http://localhost:9000"
})
public class InstrumentServiceImplIntegrationTest {

    @Autowired
    private InstrumentServiceImpl instrumentService;

    @BeforeEach
    void setUp() {
        WireMock.reset();

    }

    @Test
    void fetchAllInstruments_ShouldCallExternalSystemAndReturnInstruments() {
        //given
        stubFor(get(urlEqualTo("/gpw/tickers"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody("""
                            {
                                "results": [
                                    {
                                        "name": "ING Bank Śląski",
                                        "ticker": "INGBSK",
                                        "isin": "PLBSK0000017",
                                        "tradeCurrency": "PLN",
                                        "mic": "XWAR"
                                    }
                                ]
                            }
                        """)));

        //when
        List<InstrumentDto> instrumentsList = instrumentService.fetchAllInstruments();

        //then
        assertNotNull(instrumentsList);
        assertEquals(1,instrumentsList.size());
        assertEquals("ING Bank Śląski",instrumentsList.get(0).getName());
        assertEquals("INGBSK",instrumentsList.get(0).getTicker());
        assertEquals("PLBSK0000017",instrumentsList.get(0).getIsin());
        assertEquals("PLN",instrumentsList.get(0).getTradeCurrency());
        assertEquals("XWAR",instrumentsList.get(0).getMic());
    }

    @Test
    void fetchAllInstrumentsPrices_ShouldCallExternalSystemAndReturnInstrumentsPrices() {
        //given
        stubFor(get(urlEqualTo("/gpw/prices/current"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody("""
                            {
                                "results": [
                                    {
                                        "isin": "PLBSK0000017",
                                        "price": 314
                                    }
                                ]
                            }
                        """)));

        //when
        List<InstrumentPriceDto> instrumentsPricesList = instrumentService.fetchAllInstrumentsPrices();

        //then
        assertNotNull(instrumentsPricesList);
        assertEquals(1,instrumentsPricesList.size());
        assertEquals("PLBSK0000017",instrumentsPricesList.get(0).getIsin());
        assertEquals(new BigDecimal(314),instrumentsPricesList.get(0).getPrice());
    }
}
