package com.assets.options.entities.spread;

import com.assets.options.entities.CallOption;
import com.assets.options.entities.Option;
import com.assets.options.entities.OptionTrade;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

public class BullCallSpread extends BaseOptionSpread {

    private final Option lowerCallOption;
    private final Option upperCallOption;
    private OptionTrade lowerCallOptionTrade;
    private OptionTrade upperCallOptionTrade;

    public BullCallSpread(BigDecimal currentPrice, BigDecimal lowStrikePrice, BigDecimal highStrikePrice,
                          LocalDate now, LocalDate expirationDate, Double volatility, Double riskFree,
                          BigDecimal comission, String ticker, int contracts, boolean mini) {

        lowerCallOption = new CallOption(currentPrice, lowStrikePrice, now, expirationDate, volatility, riskFree);
        upperCallOption = new CallOption(currentPrice, highStrikePrice, now, expirationDate, volatility, riskFree);
        lowerCallOptionTrade = new OptionTrade(lowerCallOption, contracts, ticker, comission, mini);
        upperCallOptionTrade = new OptionTrade(upperCallOption, contracts * -1, ticker, comission, mini);
        setOptionTrades(Arrays.asList(lowerCallOptionTrade, upperCallOptionTrade));
    }

    public BullCallSpread(BigDecimal currentPrice, BigDecimal lowStrikePrice, BigDecimal highStrikePrice,
                          LocalDate now, LocalDate expirationDate, BigDecimal lowPremium, BigDecimal highPremium,
                          Double riskFree,BigDecimal comission, String ticker, int contracts, boolean mini) {
        lowerCallOption = new CallOption(currentPrice, lowStrikePrice, lowPremium, now, expirationDate, riskFree);
        upperCallOption = new CallOption(currentPrice, highStrikePrice, highPremium, now, expirationDate, riskFree);
        lowerCallOptionTrade = new OptionTrade(lowerCallOption, contracts, ticker, comission, mini);
        upperCallOptionTrade = new OptionTrade(upperCallOption, contracts * -1, ticker, comission, mini);
        setOptionTrades(Arrays.asList(lowerCallOptionTrade, upperCallOptionTrade));
    }

    @Override
    public BigDecimal getMaxGain() {
        return upperCallOption.getStrikePrice().subtract(lowerCallOption.getStrikePrice())
                .subtract(netPremiumPaid())
                .multiply(getMultiplier())
                .subtract(getComission());
    }

    private BigDecimal getMultiplier() {
        return upperCallOptionTrade.isMini() ? BigDecimal.ONE : BigDecimal.valueOf(100);
    }

    private BigDecimal netPremiumPaid() {
        return lowerCallOption.getPremium().subtract(upperCallOption.getPremium());
    }

    @Override
    public BigDecimal getMaxLoss() {
        return lowerCallOptionTrade.getCost().add(upperCallOptionTrade.getCost()).negate();
    }

}
