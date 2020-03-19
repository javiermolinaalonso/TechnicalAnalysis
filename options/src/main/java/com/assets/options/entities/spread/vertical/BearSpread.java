package com.assets.options.entities.spread.vertical;

import com.assets.options.entities.OptionTrade;

import java.math.BigDecimal;

public class BearSpread extends VerticalSpread {

    public BearSpread(OptionTrade lowerOption, OptionTrade upperOption) {
        super(lowerOption, upperOption);
    }

    @Override
    public BigDecimal getMaxGain() {
        return upperOptionTrade.getCost().add(lowerOptionTrade.getCost()).negate();
    }

    @Override
    public BigDecimal getMaxLoss() {
        return upperOptionTrade.getOption().getStrikePrice()
                .subtract(lowerOptionTrade.getOption().getStrikePrice())
                .subtract(netPremiumPaid())
                .multiply(getMultiplier());
    }
}
