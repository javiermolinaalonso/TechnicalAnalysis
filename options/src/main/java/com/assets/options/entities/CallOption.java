package com.assets.options.entities;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CallOption extends Option {

    public CallOption(BigDecimal currentPrice, BigDecimal strikePrice, LocalDate now, LocalDate expirationDate, Double volatility, Double riskFree) {
        super(null, OptionType.CALL, currentPrice, strikePrice, now, expirationDate, volatility, riskFree);
    }

    public CallOption(BigDecimal currentPrice, BigDecimal strikePrice, BigDecimal premium, LocalDate now, LocalDate expirationDate, Double riskFree) {
        super(null, currentPrice, strikePrice, premium, premium, OptionType.CALL, now, expirationDate, riskFree);
    }

}
