package com.assets.options.entities.spread.vertical;

import com.assets.options.entities.OptionTrade;

import java.math.BigDecimal;

abstract class CreditVerticalSpread extends VerticalSpread {

    public CreditVerticalSpread(OptionTrade lowerOption, OptionTrade upperOption) {
        super(lowerOption, upperOption);
    }

    @Override
    public BigDecimal getMaxGain() {
        return upperOptionTrade.getCost().add(lowerOptionTrade.getCost()).negate();
    }

    @Override
    public BigDecimal getMaxLoss() {
        return lowerOptionTrade.getOption().getStrikePrice()
                .subtract(upperOptionTrade.getOption().getStrikePrice())
                .subtract(netPremiumPaid())
                .multiply(getMultiplier())
                .subtract(getComission());
    }

}
