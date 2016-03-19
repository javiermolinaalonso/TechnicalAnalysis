package com.assets.options.entities;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class OptionSpread {

    private final List<OptionTrade> optionTrades;

    public OptionSpread(List<OptionTrade> optionTrades) {
        this.optionTrades = optionTrades;
    }

    public BigDecimal getExpectedValue(BigDecimal value, LocalDate when, double volatility) {
        BigDecimal expectedValue = BigDecimal.ZERO;
        for (OptionTrade optionTrade : optionTrades) {
            Option option = optionTrade.getExpectedValue(value, when, volatility);
            BigDecimal tradePremium = optionTrade.getExpirationValue(value);
            expectedValue = expectedValue.add(tradePremium);
        }
        return expectedValue;
    }
}
