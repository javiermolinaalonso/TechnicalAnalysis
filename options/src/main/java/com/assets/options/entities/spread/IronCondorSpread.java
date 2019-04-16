package com.assets.options.entities.spread;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.StringJoiner;

public class IronCondorSpread extends BaseOptionSpread {
    private final VerticalPutSpread putSpread;

    private final VerticalCallSpread callSpread;
    private double volatility;

    public static IronCondorSpread basicIronCondor(double currentPrice,
                                                   double lowLossStrike, double lowProfitStrike,
                                                   double highProfitStrike, double highStrikeLossPrice,
                                                   int daysToExpiry, double volatility, String ticker) {
        return new IronCondorSpread(currentPrice, lowLossStrike, volatility, lowProfitStrike, volatility, highProfitStrike, volatility, highStrikeLossPrice,volatility,
                LocalDate.now(), LocalDate.now().plusDays(daysToExpiry), volatility, 0.01d, BigDecimal.ONE, ticker, 1, false);
    }

    public static IronCondorSpread complexSpread(double currentPrice,
                                                 double lowLossStrike, double lowLossIV,
                                                 double lowProfitStrike, double lowProfitIV,
                                                 double highProfitStrike, double highProfitIV,
                                                 double highStrikeLossPrice, double highLossIV,
                                                 int daysToExpiry, double volatility, String ticker) {

        return new IronCondorSpread(currentPrice, lowLossStrike, lowLossIV, lowProfitStrike, lowProfitIV,
                highProfitStrike, highProfitIV, highStrikeLossPrice, highLossIV,
                LocalDate.now(), LocalDate.now().plusDays(daysToExpiry), volatility, 0.01d, BigDecimal.ONE, ticker, 1, false);
    }

    public IronCondorSpread(double currentPrice,
                            double lowLossStrike, double lowLossIV,
                            double lowProfitStrike, double lowProfitIV,
                            double highProfitStrike, double highProfitIV,
                            double highStrikeLossPrice, double highLossIV,
                            LocalDate now, LocalDate expirationDate, Double volatility, Double riskFree,
                            BigDecimal comission, String ticker, int contracts, boolean mini) {
        this(BigDecimal.valueOf(currentPrice), BigDecimal.valueOf(lowLossStrike), lowLossIV,
                BigDecimal.valueOf(lowProfitStrike), lowProfitIV,
                BigDecimal.valueOf(highProfitStrike), highProfitIV, BigDecimal.valueOf(highStrikeLossPrice), highLossIV,
                now, expirationDate, volatility, riskFree, comission, ticker, contracts, mini);
    }

    public IronCondorSpread(BigDecimal currentPrice,
                            BigDecimal lowLossStrike, double lowLossIV,
                            BigDecimal lowProfitStrike, double lowProfitIV,
                            BigDecimal highProfitStrike, double highProfitIV,
                            BigDecimal highStrikeLossPrice, double highLossIV,
                            LocalDate now, LocalDate expirationDate, Double volatility, Double riskFree,
                            BigDecimal comission, String ticker, int contracts, boolean mini) {
        super(mini);
        putSpread = new VerticalPutSpread(currentPrice, lowLossStrike, lowLossIV, lowProfitStrike, lowProfitIV, now, expirationDate, volatility, riskFree, comission, ticker, contracts, mini);
        callSpread = new VerticalCallSpread(currentPrice, highStrikeLossPrice, highLossIV, highProfitStrike, highProfitIV, now, expirationDate, volatility, riskFree, comission, ticker, contracts, mini);
        addSpread(putSpread);
        addSpread(callSpread);
        this.volatility = volatility;
    }

    @Override
    public BigDecimal getMaxGain() {
        return putSpread.getMaxGain().add(callSpread.getMaxGain());
    }


    @Override
    public BigDecimal getMaxLoss() {
        return callSpread.getMaxLoss().add(putSpread.getMaxGain().subtract(putSpread.getComission()))
                .min(
                        putSpread.getMaxLoss().add(callSpread.getMaxGain().subtract(callSpread.getComission())
                ));
    }

    @Override
    public LocalDate getExpirationDate() {
        return putSpread.getExpirationDate();
    }

    @Override
    public double getVolatility() {
        return volatility;
    }


    public boolean isValid() {
        return putSpread.getLowerPremium().compareTo(BigDecimal.ONE) > 0
                && putSpread.getUpperPremium().compareTo(BigDecimal.ONE) > 0
                && callSpread.getLowerPremium().compareTo(BigDecimal.ONE) > 0
                && callSpread.getUpperPremium().compareTo(BigDecimal.ONE) > 0;
    }
    @Override
    public String toString() {
        return new StringJoiner(", ","Iron Condor{", "}")
                .add(String.format("[%.2f/%.2f/%.2f/%.2f] @ %s, vol: %.4f", putSpread.getLowerStrike(), putSpread.getUpperStrike(), callSpread.getHighStrike(), callSpread.getLowStrike(), putSpread.getExpirationDate(), volatility))
                .add(String.format("[%.2f/%.2f/%.2f/%.2f]", putSpread.getLowerPremium(), putSpread.getUpperPremium(), callSpread.getUpperPremium(), callSpread.getLowerPremium()))
                .add(String.format("Max Loss:%.2f, Max Win:%.2f", getMaxLoss(), getMaxGain()))
                .toString();
    }
}
