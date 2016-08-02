package com.assets.options.entities.spread;

import com.assets.options.entities.OptionTrade;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseOptionSpread implements OptionSpread {

    private List<OptionTrade> optionTrades;

    public BaseOptionSpread() {
        this.optionTrades = new ArrayList<>();
    }

    public BaseOptionSpread(List<OptionTrade> optionTrades) {
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

    public BaseOptionSpread addSpread(BaseOptionSpread spread) {
        this.optionTrades.addAll(spread.getOptionTrades());
        return this;
    }

    public void setOptionTrades(List<OptionTrade> optionTrades) {
        this.optionTrades = optionTrades;
    }

    public List<OptionTrade> getOptionTrades() {
        return optionTrades;
    }

    @Override
    public BigDecimal getCost() {
        return optionTrades.stream().map(OptionTrade::getCost).reduce(BigDecimal::add).get();
    }

    public void printSpread(double from, double to, double step) {
        for(double expectedPrice = from; expectedPrice < to; expectedPrice+=step) {
            BigDecimal expectedValue = getExpirationValue(BigDecimal.valueOf(expectedPrice));
            System.out.println(String.format("%.5f", expectedValue.doubleValue()));
        }
    }
}
