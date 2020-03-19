package com.assets.options.entities;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PutOption extends Option {

    public PutOption(String ticker, BigDecimal currentPrice, BigDecimal strikePrice, LocalDate now, LocalDate expirationDate, Double volatility, Double riskFree) {
        super(ticker, OptionType.PUT, currentPrice, strikePrice, now, expirationDate, volatility, riskFree);
    }

    public PutOption(String ticker, double currentPrice, double strikePrice, double premium, LocalDate now, LocalDate expirationDate, double riskFree) {
        this(ticker, BigDecimal.valueOf(currentPrice), BigDecimal.valueOf(strikePrice), BigDecimal.valueOf(premium), now, expirationDate, riskFree);
    }

    public PutOption(String ticker, BigDecimal currentPrice, BigDecimal strikePrice, BigDecimal premium, LocalDate now, LocalDate expirationDate, Double riskFree) {
        super(ticker, OptionType.PUT, currentPrice, strikePrice, premium, premium, now, expirationDate, riskFree);
    }
    public PutOption(String ticker, BigDecimal currentPrice, BigDecimal strikePrice, BigDecimal bid, BigDecimal ask, LocalDate now, LocalDate expirationDate, Double riskFree) {
        super(ticker, OptionType.PUT, currentPrice, strikePrice, bid, ask, now, expirationDate, riskFree);
    }

}
