package com.assets.options.entities;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CallOption extends Option {

    public CallOption(String ticker, double currentPrice, double strikePrice, LocalDate now, LocalDate expirationDate, double volatility, double riskFree) {
        this(ticker, BigDecimal.valueOf(currentPrice), BigDecimal.valueOf(strikePrice), now, expirationDate, volatility, riskFree);
    }
    public CallOption(String ticker, BigDecimal currentPrice, BigDecimal strikePrice, LocalDate now, LocalDate expirationDate, double volatility, double riskFree) {
        super(ticker, OptionType.CALL, currentPrice, strikePrice, now, expirationDate, volatility, riskFree);
    }

    public CallOption(String ticker, double currentPrice, double strikePrice, double premium, LocalDate now, LocalDate expirationDate, double riskFree) {
        this(ticker, BigDecimal.valueOf(currentPrice), BigDecimal.valueOf(strikePrice), BigDecimal.valueOf(premium), now, expirationDate, riskFree);
    }

    public CallOption(String ticker, BigDecimal currentPrice, BigDecimal strikePrice, BigDecimal premium, LocalDate now, LocalDate expirationDate, double riskFree) {
        super(ticker, currentPrice, strikePrice, premium, premium, OptionType.CALL, now, expirationDate, riskFree);
    }

}
