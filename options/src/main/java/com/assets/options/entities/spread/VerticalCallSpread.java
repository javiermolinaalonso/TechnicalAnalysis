package com.assets.options.entities.spread;

import com.assets.options.entities.CallOption;
import com.assets.options.entities.Option;
import com.assets.options.entities.OptionTrade;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

public class VerticalCallSpread extends BaseOptionSpread {

    private final Option lowerOption;
    private final Option upperOption;
    private final Double volatility;
    private OptionTrade lowerOptionTrade;
    private OptionTrade upperOptionTrade;

    public static VerticalCallSpread basicCallSpread(double currentPrice,
                                                     double lowStrikePrice,
                                                     double highStrikePrice,
                                                     int daysToExpiry,
                                                     double volatility,
                                                     String ticker) {
        return new VerticalCallSpread(
                BigDecimal.valueOf(currentPrice),
                BigDecimal.valueOf(lowStrikePrice),
                volatility,
                BigDecimal.valueOf(highStrikePrice),
                volatility,
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

    public VerticalCallSpread(BigDecimal currentPrice, BigDecimal lowStrikePrice, double lowStrikeIV,
                              BigDecimal highStrikePrice, double highStrikeIV,
                              LocalDate now, LocalDate expirationDate, Double volatility, Double riskFree,
                              BigDecimal comission, String ticker, int contracts, boolean mini) {
        super(mini);
        lowerOption = new CallOption(ticker, currentPrice, lowStrikePrice, now, expirationDate, lowStrikeIV, riskFree);
        upperOption = new CallOption(ticker, currentPrice, highStrikePrice, now, expirationDate, highStrikeIV, riskFree);
        lowerOptionTrade = new OptionTrade(lowerOption, contracts, ticker, comission, mini);
        upperOptionTrade = new OptionTrade(upperOption, contracts * -1, ticker, comission, mini);
        setOptionTrades(Arrays.asList(lowerOptionTrade, upperOptionTrade));
        this.volatility = volatility;
    }

    public VerticalCallSpread(BigDecimal currentPrice, BigDecimal lowStrikePrice, BigDecimal highStrikePrice,
                              LocalDate now, LocalDate expirationDate, BigDecimal lowPremium, BigDecimal highPremium,
                              Double riskFree, BigDecimal comission, String ticker, int contracts, boolean mini) {
        super(mini);
        lowerOption = new CallOption(ticker, currentPrice, lowStrikePrice, lowPremium, now, expirationDate, riskFree);
        upperOption = new CallOption(ticker, currentPrice, highStrikePrice, highPremium, now, expirationDate, riskFree);
        lowerOptionTrade = new OptionTrade(lowerOption, contracts, ticker, comission, mini);
        upperOptionTrade = new OptionTrade(upperOption, contracts * -1, ticker, comission, mini);
        setOptionTrades(Arrays.asList(lowerOptionTrade, upperOptionTrade));
        this.volatility = (lowerOption.getImpliedVolatility() + upperOption.getImpliedVolatility()) / 2;
    }

    @Override
    public BigDecimal getMaxGain() {
        if (isDebit()) {
            return upperOption.getStrikePrice().subtract(lowerOption.getStrikePrice())
                    .subtract(netPremiumPaid())
                    .multiply(getMultiplier())
                    .subtract(getComission());
        } else {
            return upperOptionTrade.getCost().add(lowerOptionTrade.getCost()).negate();
        }
    }

    @Override
    public BigDecimal getMaxLoss() {
        if (isDebit()) {
            return lowerOptionTrade.getCost().add(upperOptionTrade.getCost()).negate();
        } else {
            return upperOption.getStrikePrice()
                    .subtract(lowerOption.getStrikePrice())
                    .subtract(netPremiumPaid())
                    .multiply(getMultiplier());
        }
    }

    @Override
    public LocalDate getExpirationDate() {
        return lowerOption.getExpirationDate();
    }

    @Override
    public double getVolatility() {
        return volatility;
    }

    public BigDecimal getHighStrike() {
        return upperOption.getStrikePrice();
    }

    public BigDecimal getLowStrike() {
        return lowerOption.getStrikePrice();
    }

    BigDecimal netPremiumPaid() {
        return lowerOption.getPremium().subtract(upperOption.getPremium());
    }

    private boolean isDebit() {
        return upperOption.getStrikePrice().compareTo(lowerOption.getStrikePrice()) > 0;
    }


    public BigDecimal getLowerPremium() {
        return lowerOption.getPremium();
    }

    public BigDecimal getUpperPremium() {
        return upperOption.getPremium();
    }
}
