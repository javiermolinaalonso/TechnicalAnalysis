package com.assets.options.entities.spread;

import com.assets.options.entities.CallOption;
import com.assets.options.entities.Option;
import com.assets.options.entities.OptionTrade;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

public class BearCallSpread extends BaseOptionSpread {

    public BearCallSpread(BigDecimal currentPrice, BigDecimal lowStrikePrice, BigDecimal highStrikePrice,
                          LocalDate now, LocalDate expirationDate, Double volatility, Double riskFree,
                          BigDecimal comission, String ticker, int contracts, boolean mini) {
        Option lowerCallOption = new CallOption(currentPrice, lowStrikePrice, now, expirationDate, volatility, riskFree);
        Option upperCallOption = new CallOption(currentPrice, highStrikePrice, now, expirationDate, volatility, riskFree);
        OptionTrade lowerCallOptionTrade = new OptionTrade(lowerCallOption, contracts * -1, ticker, comission, mini);
        OptionTrade upperCallOptionTrade = new OptionTrade(upperCallOption, contracts, ticker, comission, mini);
        setOptionTrades(Arrays.asList(lowerCallOptionTrade, upperCallOptionTrade));

    }

    @Override
    public BigDecimal getMaxGain() {
        return null;
    }

    @Override
    public BigDecimal getMaxLoss() {
        return null;
    }
}
