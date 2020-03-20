package com.assets.options.entities;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PutOption extends Option {

    public PutOption(String ticker, BigDecimal currentPrice, BigDecimal strikePrice, LocalDate now, LocalDate expirationDate, Double volatility, Double riskFree) {
        super(ticker, currentPrice, strikePrice, now, expirationDate, volatility, riskFree);
    }

    public PutOption(String ticker, BigDecimal currentPrice, BigDecimal strikePrice, BigDecimal bid, BigDecimal ask, LocalDate now, LocalDate expirationDate, Double riskFree) {
        super(ticker, currentPrice, strikePrice, bid, ask, now, expirationDate, riskFree);
    }

    @Override
    public boolean isCall() {
        return false;
    }
}
