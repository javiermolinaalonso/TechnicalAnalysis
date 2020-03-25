package com.assets.options.entities.spread.vertical;

import com.assets.options.entities.OptionTrade;
import com.assets.options.entities.portfolio.OptionPortfolio;
import com.assets.options.entities.spread.BaseOptionSpread;

import java.math.BigDecimal;
import java.time.LocalDate;

import static java.util.Arrays.asList;

public abstract class VerticalSpread extends BaseOptionSpread {

    protected OptionTrade lowerOptionTrade;
    protected OptionTrade upperOptionTrade;

    public VerticalSpread(OptionTrade lowerOption, OptionTrade upperOption) {
        super(new OptionPortfolio(asList(lowerOption, upperOption)));
        this.lowerOptionTrade = lowerOption;
        this.upperOptionTrade = upperOption;
    }

    @Override
    public LocalDate getExpirationDate() {
        return lowerOptionTrade.getOption().getExpirationDate();
    }

    @Override
    public double getVolatility() {
        return (lowerOptionTrade.getImpliedVolatility() + upperOptionTrade.getImpliedVolatility()) / 2;
    }

    public BigDecimal getHighStrike() {
        return upperOptionTrade.getOption().getStrikePrice();
    }

    public BigDecimal getLowStrike() {
        return lowerOptionTrade.getOption().getStrikePrice();
    }

    public BigDecimal getLowerPremium() {
        return lowerOptionTrade.getOption().getBid();
    }

    public BigDecimal getUpperPremium() {
        return upperOptionTrade.getOption().getAsk();
    }

}
