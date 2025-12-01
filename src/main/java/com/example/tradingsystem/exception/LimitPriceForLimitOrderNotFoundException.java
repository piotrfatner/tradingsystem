package com.example.tradingsystem.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class LimitPriceForLimitOrderNotFoundException extends RuntimeException {
    public LimitPriceForLimitOrderNotFoundException(String isin) {
        super(String.format("Limit price is required for LMT order - isin: '%s'", isin));
    }
}
