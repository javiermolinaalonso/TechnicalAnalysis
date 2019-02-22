package com.assets.options.entities.spread;

import java.math.BigDecimal;
import java.time.LocalDate;

public class IronCondorSpread extends BaseOptionSpread {

    private final VerticalPutSpread putSpread;
    private final VerticalCallSpread callSpread;
    public IronCondorSpread(double currentPrice,
                            double lowLossStrike, double lowProfitStrike,
                            double highProfitStrike, double highStrikeLossPrice,
                            LocalDate now, LocalDate expirationDate, Double volatility, Double riskFree,
                            BigDecimal comission, String ticker, int contracts, boolean mini) {
        this(BigDecimal.valueOf(currentPrice), BigDecimal.valueOf(lowLossStrike), BigDecimal.valueOf(lowProfitStrike),
                BigDecimal.valueOf(highProfitStrike), BigDecimal.valueOf(highStrikeLossPrice),
                now, expirationDate, volatility, riskFree, comission, ticker, contracts, mini);
    }
    public IronCondorSpread(BigDecimal currentPrice,
                            BigDecimal lowLossStrike, BigDecimal lowProfitStrike,
                            BigDecimal highProfitStrike, BigDecimal highStrikeLossPrice,
                            LocalDate now, LocalDate expirationDate, Double volatility, Double riskFree,
                            BigDecimal comission, String ticker, int contracts, boolean mini) {
        putSpread = new VerticalPutSpread(currentPrice, lowLossStrike, lowProfitStrike, now, expirationDate, volatility, riskFree, comission, ticker, contracts, mini);
        callSpread = new VerticalCallSpread(currentPrice, highStrikeLossPrice, highProfitStrike, now, expirationDate, volatility, riskFree, comission, ticker, contracts, mini);
        addSpread(putSpread);
        addSpread(callSpread);
    }

    @Override
    public BigDecimal getMaxGain() {
        return putSpread.getMaxGain().add(callSpread.getMaxGain());
    }

    @Override
    public BigDecimal getMaxLoss() {
        return putSpread.getMaxLoss().add(putSpread.getMaxLoss());
    }
}
