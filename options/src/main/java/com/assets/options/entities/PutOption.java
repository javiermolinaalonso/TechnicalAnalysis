package com.assets.options.entities;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PutOption extends Option {

    public PutOption(BigDecimal currentPrice, BigDecimal strikePrice, LocalDate now, LocalDate expirationDate, Double volatility, Double riskFree) {
        super(currentPrice, strikePrice, now, expirationDate, volatility, riskFree);
    }

    @Override
    protected boolean isCall() {
        return false;
    }
}
