package com.assets.options.entities.spread;

import com.assets.options.entities.Option;
import com.assets.options.entities.OptionTrade;
import com.assets.options.entities.PutOption;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

public class VerticalPutSpread extends BaseOptionSpread {

    private final Option lowerOption;
    private final Option upperOption;
    private final double volatility;
    private OptionTrade lowerOptionTrade;
    private OptionTrade upperOptionTrade;


    public static VerticalPutSpread basicPutSpread(double currentPrice,
                                                   double lowStrikePrice,
                                                   double highStrikePrice,
                                                   int daysToExpiry,
                                                   double volatility,
                                                   String ticker) {
        return new VerticalPutSpread(
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



    public VerticalPutSpread(BigDecimal currentPrice, BigDecimal lowStrikePrice, BigDecimal highStrikePrice,
                             BigDecimal lowStrikePremium, BigDecimal highStrikePremium,
                             LocalDate now, LocalDate expirationDate, Double riskFree,
                             BigDecimal comission, String ticker, int contracts, boolean mini) {
        super(mini);
        lowerOption = new PutOption(null, currentPrice, lowStrikePrice, lowStrikePremium, now, expirationDate, riskFree);
        upperOption = new PutOption(null, currentPrice, highStrikePrice, highStrikePremium, now, expirationDate, riskFree);
        doSpread(comission, ticker, contracts, mini, lowerOption, upperOption);
        this.volatility = (lowerOption.getImpliedVolatility() + upperOption.getImpliedVolatility()) / 2;
    }

    public VerticalPutSpread(BigDecimal currentPrice, BigDecimal lowStrikePrice, double lowStrikeIV,
                             BigDecimal highStrikePrice, double highStrikeIV,
                             LocalDate now, LocalDate expirationDate, Double volatility, Double riskFree,
                             BigDecimal comission, String ticker, int contracts, boolean mini) {
        super(mini);
        lowerOption = new PutOption(null, currentPrice, lowStrikePrice, now, expirationDate, lowStrikeIV, riskFree);
        upperOption = new PutOption(null, currentPrice, highStrikePrice, now, expirationDate, highStrikeIV, riskFree);
        doSpread(comission, ticker, contracts, mini, lowerOption, upperOption);
        this.volatility = volatility;
    }

    private void doSpread(BigDecimal comission, String ticker, int contracts, boolean mini, Option lowerOption, Option upperOption) {
        lowerOptionTrade = new OptionTrade(lowerOption, contracts, ticker, comission, mini);
        upperOptionTrade = new OptionTrade(upperOption, contracts * -1, ticker, comission, mini);
        setOptionTrades(Arrays.asList(lowerOptionTrade, upperOptionTrade));
    }

    @Override
    public BigDecimal getMaxGain() {
        if (isDebit()) {
            return lowerOption.getStrikePrice()
                    .subtract(upperOption.getStrikePrice())
                    .subtract(netPremiumPaid())
                    .multiply(getMultiplier())
                    .subtract(getComission());
        } else {
            return upperOptionTrade.getCost().add(lowerOptionTrade.getCost()).negate();
        }
    }


    private boolean isDebit() {
        return upperOption.getStrikePrice().compareTo(lowerOption.getStrikePrice()) < 0;
    }

    BigDecimal netPremiumPaid() {
        return lowerOption.getPremium().subtract(upperOption.getPremium());
    }

    @Override
    public BigDecimal getMaxLoss() {
        if (isDebit()) {
            return lowerOptionTrade.getCost().add(upperOptionTrade.getCost()).negate();
        } else {
            return lowerOption.getStrikePrice()
                    .subtract(upperOption.getStrikePrice())
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

    public BigDecimal getLowerStrike () {
        return lowerOption.getStrikePrice();
    }

    public BigDecimal getUpperStrike () {
        return upperOption.getStrikePrice();
    }

    public BigDecimal getLowerPremium() {
        return lowerOption.getPremium();
    }

    public BigDecimal getUpperPremium() {
        return upperOption.getPremium();
    }
}
