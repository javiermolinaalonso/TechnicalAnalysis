package com.assets.options.entities.spread;

import com.assets.options.entities.CallOption;
import com.assets.options.entities.OptionBuilder;
import com.assets.options.entities.OptionTrade;
import com.assets.options.entities.portfolio.OptionPortfolio;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.StringJoiner;

import static java.util.Arrays.asList;

public class CalendarCallSpread extends BaseOptionSpread {

    private final OptionTrade closerOption;
    private final OptionTrade furtherOption;

    public CalendarCallSpread(OptionTrade closerOption, OptionTrade furtherOption) {
        super(new OptionPortfolio(asList(closerOption, furtherOption)));
        this.closerOption = closerOption;
        this.furtherOption = furtherOption;
    }

    @Override
    public BigDecimal getMaxGain() {
        CallOption soldFurtherOption = OptionBuilder.create(closerOption.getTicker(), closerOption.getOption().getStrikePrice())
                .withStrikePrice(closerOption.getOption().getStrikePrice())
                .withIV(furtherOption.getImpliedVolatility())
                .withExpirationAt(furtherOption.getOption().getExpirationDate())
                .withCurrentDate(closerOption.getOption().getExpirationDate())
                .buildCall();
        OptionTrade writeFurtherTrade = OptionTradeFactory.write(soldFurtherOption, furtherOption.getContracts());
        return closerOption.getCost().add(writeFurtherTrade.getCost()).add(furtherOption.getCost()).negate();
    }

    @Override
    public BigDecimal getMaxLoss() {
        return getCost().negate();
    }

    @Override
    public BigDecimal getExpirationValue(BigDecimal value) {
        return getValueAt(value, closerOption.getOption().getExpirationDate());
    }

    @Override
    public LocalDate getExpirationDate() {
        return closerOption.getOption().getExpirationDate();
    }

    @Override
    public double getVolatility() {
        return (closerOption.getImpliedVolatility() + furtherOption.getImpliedVolatility()) / 2d;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", "Calendar {", "}")
                .add(String.format("Strike [%.2f @ %s for %.2f / %.2f @ %s for %.2f]", closerOption.getOption().getStrikePrice(), closerOption.getOption().getExpirationDate(), closerOption.getPremium(), furtherOption.getOption().getStrikePrice(), furtherOption.getOption().getExpirationDate(), furtherOption.getPremium()))
                .add(String.format("IV [%.2f%%, %.2f%%]", closerOption.getImpliedVolatility() * 100, furtherOption.getImpliedVolatility() * 100))
                .add(String.format("Cost [%.2f]", getCost()))
                .add(String.format("Max Loss:%.2f, Max Win:%.2f", getMaxLoss(), getMaxGain()))
                .toString();
    }
}
