package com.example.tradingsystem.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class CurrencyNotMatchingException extends RuntimeException {
    public CurrencyNotMatchingException(String dtoCurrency, String instrumentCurrency) {
        super(String.format("Provided currency '%s' does not match instrument currency: '%s'", dtoCurrency,
                instrumentCurrency));
    }
}
