package com.assets.options.entities;

import java.math.BigDecimal;
import java.time.LocalDate;

public class OptionTrade {

    private final Option option;
    private final BigDecimal premium;
    private final int contracts;
    private final String ticker;
    private final BigDecimal cost;
    private final boolean mini;
    private final BigDecimal tradeComission;

    public OptionTrade(Option option, int contracts, String ticker, BigDecimal contractComission, boolean mini) {
        this.option = option;
        this.contracts = contracts;
        this.ticker = ticker;
        this.mini = mini;
        this.premium = getPremium();
        if (premium.compareTo(BigDecimal.ZERO) == 0) {
            throw new OptionIsNotAvailableException();
        }
        this.tradeComission = contractComission.multiply(BigDecimal.valueOf(Math.abs(contracts)));
        this.cost = BigDecimal.valueOf(contracts).multiply(premium).multiply(getAmountOfStocks()).add(tradeComission);
    }

    public BigDecimal getTradeComission() {
        return tradeComission;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public BigDecimal getPremium() {
        if (contracts > 0) {
            return option.getAsk();
        } else {
            return option.getBid();
        }
    }

    public int getContracts() {
        return contracts;
    }

    public String getTicker() {
        return ticker;
    }

    public BigDecimal getExpirationValue(BigDecimal value) {
        return getExpectedValue(value, option.getExpirationDate());
    }

    public BigDecimal getExpectedValue(BigDecimal value, LocalDate when) {
        return getExpectedValue(value, when, option.getImpliedVolatility());
    }

    public BigDecimal getExpectedValue(BigDecimal value, LocalDate when, double iv) {
//        BigDecimal premiumAt = new OptionTrade(getExpectedOption(value, when, iv), contracts, ticker, tradeComission.divide(BigDecimal.valueOf(contracts), 0, RoundingMode.HALF_UP), mini).getPremium();
        final BigDecimal premiumAt = getExpectedOption(value, when, iv).getBid();
        return premiumAt.subtract(this.premium)
                .multiply(getAmountOfStocks())
                .multiply(BigDecimal.valueOf(getContracts()))
                .subtract(getTradeComission());

    }

    public boolean isMini() {
        return mini;
    }

    public Option getExpectedOption(BigDecimal value, LocalDate when, double volatility) {
        OptionBuilder optionBuilder = OptionBuilder.create(ticker, value).withStrikePrice(option.getStrikePrice()).withCurrentDate(when).withExpirationAt(option.getExpirationDate()).withIV(volatility).withRiskFree(option.getRiskFree());
        if (option.isCall()) {
            return optionBuilder.buildCall();
        } else {
            return optionBuilder.buildPut();
        }
    }

    private BigDecimal getAmountOfStocks() {
        return mini ? BigDecimal.ONE : BigDecimal.valueOf(100);
    }

    public Option getOption() {
        return option;
    }

    public double getImpliedVolatility() {
        return option.getImpliedVolatility();
    }

    public Greeks getGreeks() {
        final Greeks greeks = option.getGreeks();
        return new Greeks(greeks.getDelta() * greekModifier(),
                greeks.getGamma() * greekModifier(),
                greeks.getVega() * greekModifier(),
                greeks.getTheta() * greekModifier(),
                greeks.getRho() * greekModifier());
    }

    private int greekModifier() {
        if (getContracts() < 0) {
            return -1;
        } else {
            return 1;
        }
    }

    public BigDecimal getGrossPremium() {
        return getPremium().multiply(BigDecimal.valueOf(getContracts()));
    }

    @Override
    public String toString() {
        return "OptionTrade{" +
                "option=" + option +
                ", premium=" + premium +
                ", contracts=" + contracts +
                ", ticker='" + ticker + '\'' +
                ", cost=" + cost +
                ", mini=" + mini +
                ", tradeComission=" + tradeComission +
                '}';
    }
}
