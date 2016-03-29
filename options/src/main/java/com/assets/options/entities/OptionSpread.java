package com.assets.options.entities;

import java.math.BigDecimal;
import java.util.List;

public class OptionSpread {

    private final List<OptionTrade> optionTrades;

    public OptionSpread(List<OptionTrade> optionTrades) {
        this.optionTrades = optionTrades;
    }

    public BigDecimal getExpirationValue(BigDecimal value) {
        BigDecimal expectedValue = BigDecimal.ZERO;
        for (OptionTrade optionTrade : optionTrades) {
            BigDecimal tradePremium = optionTrade.getExpirationValue(value);
            expectedValue = expectedValue.add(tradePremium);
        }
        return expectedValue;
    }
}
