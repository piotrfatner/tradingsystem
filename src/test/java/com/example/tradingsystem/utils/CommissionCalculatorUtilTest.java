package com.example.tradingsystem.utils;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CommissionCalculatorUtilTest {

    @Test
    void shouldCalculateCommissionForXwarBelowMinimalAmount() {
        //given
        BigDecimal executionPrice = new BigDecimal("100");
        int quantity = 10; // transaction = 1000
        // rate 0.3% * 1000 -> 3.00
        BigDecimal expectedCommission = new BigDecimal("3").setScale(2, RoundingMode.HALF_UP);

        //when
        BigDecimal result = CommissionCalculatorUtil.getCalculatedCommission(
                "XWAR",
                executionPrice,
                quantity
        );

        //then
        assertEquals(new BigDecimal("5").setScale(2, RoundingMode.HALF_UP), result);
    }
    @Test
    void shouldCalculateCommissionForXwarAboveMinimalAmount() {
        //given
        BigDecimal price = new BigDecimal("300");
        int quantity = 10; // transaction = 3000
        // rate 0.3% * 3000 -> 9.00
        BigDecimal expectedCommission = new BigDecimal("9").setScale(2, RoundingMode.HALF_UP);

        //when
        BigDecimal result = CommissionCalculatorUtil.getCalculatedCommission(
                "XWAR",
                price,
                quantity
        );

        //then
        assertEquals(expectedCommission, result);
    }

    @Test
    void shouldCalculateCommissionForOtherMarketBelowMinimalAmount() {
        //given
        BigDecimal executionPrice = new BigDecimal("100");
        int quantity = 10; // transaction = 1000
        // rate 0.2% * 1000 = 2.00 -> below minimal (10)
        BigDecimal expectedCommission = new BigDecimal("2").setScale(2, RoundingMode.HALF_UP);

        //when
        BigDecimal result = CommissionCalculatorUtil.getCalculatedCommission(
                "NASDAQ",
                executionPrice,
                quantity
        );

        //then
        assertEquals(new BigDecimal("10").setScale(2, RoundingMode.HALF_UP), result);
    }

    @Test
    void shouldCalculateCommissionForOtherMarketAboveMinimalAmount() {
        //given
        BigDecimal executionPrice = new BigDecimal("1000");
        int quantity = 10; // transaction = 10000
        // rate 0.2% * 10000 = 20.00 -> above minimal (10)
        BigDecimal expectedCommission = new BigDecimal("20").setScale(2, RoundingMode.HALF_UP);

        //when
        BigDecimal result = CommissionCalculatorUtil.getCalculatedCommission(
                "NYSE",
                executionPrice,
                quantity
        );

        //then
        assertEquals(expectedCommission, result);
    }

    @Test
    void shouldCalculateCommissionForOtherMarketExactlyMinimalAmount() {
        //given
        BigDecimal executionPrice = new BigDecimal("500");
        int quantity = 10; // transaction = 5000
        // rate 0.2% * 5000 = 10 exactly
        BigDecimal expectedCommission = new BigDecimal("10").setScale(2, RoundingMode.HALF_UP);

        //when
        BigDecimal result = CommissionCalculatorUtil.getCalculatedCommission(
                "LSE",
                executionPrice,
                quantity
        );

        //then
        assertEquals(expectedCommission, result);
    }
}
