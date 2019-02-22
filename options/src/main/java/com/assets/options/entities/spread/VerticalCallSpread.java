package com.assets.options.entities.spread;

import com.assets.options.entities.CallOption;
import com.assets.options.entities.Option;
import com.assets.options.entities.OptionTrade;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

public class VerticalCallSpread extends BaseOptionSpread {

    private final Option lowerCallOption;
    private final Option upperCallOption;
    private OptionTrade lowerCallOptionTrade;
    private OptionTrade upperCallOptionTrade;

    public static VerticalCallSpread basicCallSpread(double currentPrice,
                                                     double lowStrikePrice,
                                                     double highStrikePrice,
                                                     int daysToExpiry,
                                                     double volatility,
                                                     String ticker) {
        return new VerticalCallSpread(
                BigDecimal.valueOf(currentPrice),
                BigDecimal.valueOf(lowStrikePrice),
                BigDecimal.valueOf(highStrikePrice),
                LocalDate.now(),
                LocalDate.now().plusDays(daysToExpiry),
                volatility,
                0.001d,
                BigDecimal.ONE,
                ticker,
                1,
                false
        );
    }

    public VerticalCallSpread(BigDecimal currentPrice, BigDecimal lowStrikePrice, BigDecimal highStrikePrice,
                              LocalDate now, LocalDate expirationDate, Double volatility, Double riskFree,
                              BigDecimal comission, String ticker, int contracts, boolean mini) {

        lowerCallOption = new CallOption(ticker, currentPrice, lowStrikePrice, now, expirationDate, volatility, riskFree);
        upperCallOption = new CallOption(ticker, currentPrice, highStrikePrice, now, expirationDate, volatility, riskFree);
        lowerCallOptionTrade = new OptionTrade(lowerCallOption, contracts, ticker, comission, mini);
        upperCallOptionTrade = new OptionTrade(upperCallOption, contracts * -1, ticker, comission, mini);
        setOptionTrades(Arrays.asList(lowerCallOptionTrade, upperCallOptionTrade));
    }

    public VerticalCallSpread(BigDecimal currentPrice, BigDecimal lowStrikePrice, BigDecimal highStrikePrice,
                              LocalDate now, LocalDate expirationDate, BigDecimal lowPremium, BigDecimal highPremium,
                              Double riskFree, BigDecimal comission, String ticker, int contracts, boolean mini) {
        lowerCallOption = new CallOption(ticker, currentPrice, lowStrikePrice, lowPremium, now, expirationDate, riskFree);
        upperCallOption = new CallOption(ticker, currentPrice, highStrikePrice, highPremium, now, expirationDate, riskFree);
        lowerCallOptionTrade = new OptionTrade(lowerCallOption, contracts, ticker, comission, mini);
        upperCallOptionTrade = new OptionTrade(upperCallOption, contracts * -1, ticker, comission, mini);
        setOptionTrades(Arrays.asList(lowerCallOptionTrade, upperCallOptionTrade));
    }

    @Override
    public BigDecimal getMaxGain() {
        if (isDebit()) {
            return upperCallOption.getStrikePrice().subtract(lowerCallOption.getStrikePrice())
                    .subtract(netPremiumPaid())
                    .multiply(getMultiplier())
                    .subtract(getComission());
        } else {
            return netPremiumPaid().multiply(getMultiplier()).negate();
        }
    }

    private BigDecimal getMultiplier() {
        return upperCallOptionTrade.isMini() ? BigDecimal.ONE : BigDecimal.valueOf(100);
    }

    private BigDecimal netPremiumPaid() {
        return lowerCallOption.getPremium().subtract(upperCallOption.getPremium());
    }

    @Override
    public BigDecimal getMaxLoss() {
        if (isDebit()) {
            return lowerCallOptionTrade.getCost().add(upperCallOptionTrade.getCost()).negate();
        } else {
            return upperCallOption.getStrikePrice()
                    .subtract(lowerCallOption.getStrikePrice())
                    .subtract(netPremiumPaid())
                    .multiply(getMultiplier());
        }
    }

    private boolean isDebit() {
        return upperCallOption.getStrikePrice().compareTo(lowerCallOption.getStrikePrice()) > 0;
    }

}
