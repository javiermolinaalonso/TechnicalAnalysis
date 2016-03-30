package com.assets.options.entities;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CallOption extends Option {

    public CallOption(BigDecimal currentPrice, BigDecimal strikePrice, LocalDate now, LocalDate expirationDate, Double volatility, Double riskFree) {
        super(currentPrice, strikePrice, now, expirationDate, volatility, riskFree);
    }

    public CallOption(BigDecimal currentPrice, BigDecimal strikePrice, BigDecimal premium, LocalDate now, LocalDate expirationDate, Double riskFree) {
        super(currentPrice, strikePrice, premium, now, expirationDate, riskFree);
    }

    @Override
    protected boolean isCall() {
        return true;
    }
}
