package com.assets.options.entities.spread.vertical;

import com.assets.options.entities.OptionTrade;

import java.math.BigDecimal;

public class BullSpread extends VerticalSpread {

    public BullSpread(OptionTrade lowerOption, OptionTrade upperOption) {
        super(lowerOption, upperOption);
    }

    @Override
    public BigDecimal getMaxGain() {
        return upperOptionTrade.getOption().getStrikePrice().subtract(lowerOptionTrade.getOption().getStrikePrice())
                .subtract(netPremiumPaid())
                .multiply(getMultiplier())
                .subtract(getComission());
    }


    @Override
    public BigDecimal getMaxLoss() {
        return lowerOptionTrade.getCost().add(upperOptionTrade.getCost()).negate();
    }
}
