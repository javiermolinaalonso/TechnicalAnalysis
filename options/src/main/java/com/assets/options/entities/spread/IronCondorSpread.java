package com.assets.options.entities.spread;

import java.math.BigDecimal;
import java.time.LocalDate;

public class IronCondorSpread extends BaseOptionSpread {

    public IronCondorSpread(BigDecimal currentPrice,
                            BigDecimal lowLossStrike, BigDecimal lowProfitStrike,
                            BigDecimal highProfitStrike, BigDecimal highStrikeLossPrice,
                            LocalDate now, LocalDate expirationDate, Double volatility, Double riskFree,
                            BigDecimal comission, String ticker, int contracts, boolean mini) {
        BaseOptionSpread bullPutSpread = new BullPutSpread(currentPrice, lowLossStrike, lowProfitStrike, now, expirationDate, volatility, riskFree, comission, ticker, contracts, mini);
        BaseOptionSpread bearCallSpread = new BearCallSpread(currentPrice, highProfitStrike, highStrikeLossPrice, now, expirationDate, volatility, riskFree, comission, ticker, contracts, mini);
        addSpread(bullPutSpread);
        addSpread(bearCallSpread);
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
