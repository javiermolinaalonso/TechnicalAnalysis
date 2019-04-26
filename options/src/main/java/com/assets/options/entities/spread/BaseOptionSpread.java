package com.assets.options.entities.spread;

import com.assets.options.entities.Greeks;
import com.assets.options.entities.OptionTrade;
import com.assets.options.entities.portfolio.OptionPortfolio;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseOptionSpread implements OptionSpread {

    private OptionPortfolio options;
    private boolean mini = false;

    BaseOptionSpread(boolean mini) {
        this(new OptionPortfolio(new ArrayList<>()));
        this.mini = mini;
    }

    private BaseOptionSpread(OptionPortfolio portfolio) {
        this.options = portfolio;
    }

    protected BigDecimal getMultiplier() {
        return mini ? BigDecimal.ONE : BigDecimal.valueOf(100);
    }

    public BigDecimal getValueAt(BigDecimal value, LocalDate when, double volatility) {
        BigDecimal expectedValue = BigDecimal.ZERO;
        for (OptionTrade optionTrade : options.getTrades()) {
            final BigDecimal premium = optionTrade.getExpectedValue(value, when, volatility);
            expectedValue = expectedValue.add(premium);
        }
        return expectedValue;
    }

    public BigDecimal getValueAt(BigDecimal value, LocalDate when) {
        BigDecimal expectedValue = BigDecimal.ZERO;
        for (OptionTrade optionTrade : options.getTrades()) {
            final BigDecimal premium = optionTrade.getExpectedValue(value, when);
            expectedValue = expectedValue.add(premium);
        }
        return expectedValue;
    }

    public BigDecimal getExpirationValue(BigDecimal value) {
        BigDecimal expectedValue = BigDecimal.ZERO;
        for (OptionTrade optionTrade : options.getTrades()) {
            BigDecimal tradePremium = optionTrade.getExpirationValue(value);
            expectedValue = expectedValue.add(tradePremium);
        }
        return expectedValue;
    }

    BaseOptionSpread addSpread(BaseOptionSpread spread) {
        this.options.add(spread.getOptionTrades());
        return this;
    }

    void setOptionTrades(List<OptionTrade> optionTrades) {
        this.options.setTrades(optionTrades);
    }

    public List<OptionTrade> getOptionTrades() {
        return options.getTrades();
    }

    @Override
    public BigDecimal getCost() {
        return options.getTrades().stream().map(OptionTrade::getCost).reduce(BigDecimal::add).get();
    }

    @Override
    public BigDecimal getCost(BigDecimal currentPrice, LocalDate when, double volatility) {
        return options.getCost(when, currentPrice, volatility);
    }

    @Override
    public BigDecimal getComission() {
        return options.getTrades().stream().map(OptionTrade::getContractComission).reduce(BigDecimal::add).get();
    }

    @Override
    public Greeks getGreeks() {
        return options.getGreeks();
    }

    public void printSpread(double from, double to, double step) {
        for(double expectedPrice = from; expectedPrice < to; expectedPrice+=step) {
            BigDecimal expectedValue = getExpirationValue(BigDecimal.valueOf(expectedPrice));
            System.out.println(String.format("%.5f", expectedValue.doubleValue()));
        }
    }

    @Override
    public BigDecimal getStrikePriceAverage() {
        return BigDecimal.valueOf(options.getTrades().stream()
                .mapToDouble(x -> x.getOption().getStrikePrice().doubleValue())
                .average()
                .orElse(0d));
    }

}
