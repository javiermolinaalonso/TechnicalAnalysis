package com.assets.options.entities.spread;

import com.assets.options.entities.OptionTrade;

import java.math.BigDecimal;
import java.util.List;

public abstract class OptionSpread {

    private List<OptionTrade> optionTrades;

    public OptionSpread() {
    }

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

    public void setOptionTrades(List<OptionTrade> optionTrades) {
        this.optionTrades = optionTrades;
    }
}
