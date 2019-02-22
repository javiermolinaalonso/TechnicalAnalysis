package com.assets.options.entities;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CallOption extends Option {

    public CallOption(String ticker, BigDecimal currentPrice, BigDecimal strikePrice, LocalDate now, LocalDate expirationDate, Double volatility, Double riskFree) {
        super(ticker, OptionType.CALL, currentPrice, strikePrice, now, expirationDate, volatility, riskFree);
    }

    public CallOption(String ticker, BigDecimal currentPrice, BigDecimal strikePrice, BigDecimal premium, LocalDate now, LocalDate expirationDate, Double riskFree) {
        super(ticker, currentPrice, strikePrice, premium, premium, OptionType.CALL, now, expirationDate, riskFree);
    }

}
