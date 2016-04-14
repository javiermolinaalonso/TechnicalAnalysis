package com.assets.options.entities;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PutOption extends Option {

    public PutOption(BigDecimal currentPrice, BigDecimal strikePrice, LocalDate now, LocalDate expirationDate, Double volatility, Double riskFree) {
        super(null, OptionType.PUT, currentPrice, strikePrice, now, expirationDate, volatility, riskFree);
    }

    public PutOption(BigDecimal currentPrice, BigDecimal strikePrice, BigDecimal premium, LocalDate now, LocalDate expirationDate, Double riskFree) {
        super(null, currentPrice, strikePrice, premium, premium, OptionType.PUT, now, expirationDate, riskFree);
    }

}
