package com.assets.options.entities;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CallOption extends Option {

    CallOption(String ticker, BigDecimal currentPrice, BigDecimal strikePrice, LocalDate now, LocalDate expirationDate, double volatility, double riskFree) {
        super(ticker, OptionType.CALL, currentPrice, strikePrice, now, expirationDate, volatility, riskFree);
    }

    CallOption(String ticker, BigDecimal currentPrice, BigDecimal strikePrice, BigDecimal bid, BigDecimal ask, LocalDate now, LocalDate expirationDate, double riskFree) {
        super(ticker, OptionType.CALL, currentPrice, strikePrice, bid, ask, now, expirationDate, riskFree);
    }

}
