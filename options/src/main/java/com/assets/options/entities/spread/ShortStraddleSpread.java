package com.assets.options.entities.spread;

import com.assets.options.entities.Option;
import com.assets.options.entities.OptionTrade;
import com.assets.options.entities.portfolio.OptionPortfolio;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class ShortStraddleSpread extends BaseOptionSpread {

    private final OptionTrade call;
    private final OptionTrade put;

    public ShortStraddleSpread(Option call, Option put) {
        super(new OptionPortfolio(
                        List.of(
                                OptionTradeFactory.write(call, 1),
                                OptionTradeFactory.write(put, 1))
                )
        );
        this.call = OptionTradeFactory.write(call, 1);
        this.put = OptionTradeFactory.write(put, 1);
    }

    @Override
    public BigDecimal getMaxGain() {
        return call.getCost().add(put.getCost()).negate();
    }

    @Override
    public BigDecimal getMaxLoss() {
        return BigDecimal.valueOf(Double.POSITIVE_INFINITY);
    }

    @Override
    public LocalDate getExpirationDate() {
        return call.getOption().getExpirationDate();
    }

    @Override
    public double getVolatility() {
        return (call.getImpliedVolatility() + put.getImpliedVolatility()) / 2;
    }

    @Override
    public BigDecimal getMargin() {
        return getStrikePriceAverage().multiply(BigDecimal.valueOf(15)).add(put.getCost().abs()); //15%
    }
}
