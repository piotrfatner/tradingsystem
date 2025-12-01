package com.example.tradingsystem.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class CommissionCalculatorUtil {
    private static final String MIC_XWAR = "XWAR";
    private static final BigDecimal XWAR_COMMISSION_RATE = new BigDecimal("0.003");
    private static final BigDecimal OTHER_COMMISSION_RATE = new BigDecimal("0.002");
    private static final Integer MINIMAL_XWAR_COMMISSION = 5;
    private static final Integer MINIMAL_OTHERS_COMMISSSION = 10;

    public static BigDecimal getCalculatedCommission(String mic, BigDecimal executionPrice, Integer quantity) {
        Objects.requireNonNull(mic, "MIC cannot be null");
        Objects.requireNonNull(executionPrice, "Price cannot be null");
        Objects.requireNonNull(quantity, "Quantity cannot be null");
        if (MIC_XWAR.equals(mic)) {
            BigDecimal transactionValue = executionPrice.multiply(new BigDecimal(quantity));
            BigDecimal minimalCommission = new BigDecimal(MINIMAL_XWAR_COMMISSION);
            BigDecimal commission = transactionValue.multiply(XWAR_COMMISSION_RATE);
            return commission.compareTo(minimalCommission) >= 0
                    ? commission.setScale(2, RoundingMode.HALF_UP) : minimalCommission.setScale(2, RoundingMode.HALF_UP);
        }
        BigDecimal transactionValue = executionPrice.multiply(new BigDecimal(quantity));
        BigDecimal minimalCommission = new BigDecimal(MINIMAL_OTHERS_COMMISSSION);
        BigDecimal commission = transactionValue.multiply(OTHER_COMMISSION_RATE);
        return commission.compareTo(minimalCommission) >= 0
                ? commission.setScale(2, RoundingMode.HALF_UP) : minimalCommission.setScale(2, RoundingMode.HALF_UP);
    }
}
