package com.assets.options.entities.spread.neutral;

import com.assets.options.entities.portfolio.OptionPortfolio;
import com.assets.options.entities.spread.BaseOptionSpread;
import com.assets.options.entities.spread.vertical.BearCallSpread;
import com.assets.options.entities.spread.vertical.BullPutSpread;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class IronCondorSpread extends BaseOptionSpread {

    private final BearCallSpread callSpread;
    private final BullPutSpread putSpread;

    public IronCondorSpread(BearCallSpread callSpread, BullPutSpread putSpread) {
        super(new OptionPortfolio(Stream.concat(callSpread.getOptionTrades().stream(), putSpread.getOptionTrades().stream()).collect(Collectors.toList())));
        this.callSpread = callSpread;
        this.putSpread = putSpread;
    }

    @Override
    public BigDecimal getMaxGain() {
        return putSpread.getMaxGain().add(callSpread.getMaxGain());
    }

    @Override
    public BigDecimal getMaxLoss() {
        return callSpread.getMaxLoss().add(putSpread.getMaxGain())
                .min(putSpread.getMaxLoss().add(callSpread.getMaxGain()));
    }

    @Override
    public LocalDate getExpirationDate() {
        return putSpread.getExpirationDate();
    }

    @Override
    public double getVolatility() {
        return (callSpread.getVolatility() + putSpread.getVolatility()) / 2d;
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
                .add(String.format("[%.2f/%.2f/%.2f/%.2f] @ %s, vol: %.4f", putSpread.getLowStrike(), putSpread.getHighStrike(), callSpread.getHighStrike(), callSpread.getLowStrike(), putSpread.getExpirationDate(), getVolatility()))
                .add(String.format("[%.2f/%.2f/%.2f/%.2f]", putSpread.getLowerPremium(), putSpread.getUpperPremium(), callSpread.getUpperPremium(), callSpread.getLowerPremium()))
                .add(String.format("Max Loss:%.2f, Max Win:%.2f", getMaxLoss(), getMaxGain()))
                .toString();
    }
}
