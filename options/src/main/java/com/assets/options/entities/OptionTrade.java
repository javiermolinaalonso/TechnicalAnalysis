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
    private final BigDecimal contractComission;

    public OptionTrade(Option option, int contracts, String ticker, BigDecimal contractComission, boolean mini) {
        this.option = option;
        this.premium = option.getPremium();
        this.contracts = contracts;
        this.ticker = ticker;
        this.mini = mini;
        this.cost = BigDecimal.valueOf(contracts).multiply(premium).multiply(getAmountOfStocks())
                .add(contractComission.multiply(BigDecimal.valueOf(Math.abs(contracts))));
        this.contractComission = contractComission;
    }

    public BigDecimal getContractComission() {
        return contractComission;
    }

    public BigDecimal getTradeComission() {
        return BigDecimal.valueOf(Math.abs(getContracts())).multiply(getContractComission());
    }

    public BigDecimal getCost() {
        return cost;
    }

    public Option getExpectedOption() {
        return option;
    }

    public BigDecimal getPremium() {
        return premium;
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
        final BigDecimal premiumAt = getExpectedOption(value, when, option.getVolatility()).getPremium();

        return premiumAt.subtract(premium)
                .multiply(getAmountOfStocks())
                .multiply(BigDecimal.valueOf(getContracts()))
                .subtract(getTradeComission());

    }

    public boolean isMini() {
        return mini;
    }

    public Option getExpectedOption(BigDecimal value, LocalDate when, double volatility) {
        Option newOption;
        if (option.isCall()) {
            newOption = new CallOption(ticker, value, option.getStrikePrice(), when, option.getExpirationDate(), volatility, option.getRiskFree());
        } else {
            newOption = new PutOption(value, option.getStrikePrice(), when, option.getExpirationDate(), volatility, option.getRiskFree());
        }
        return newOption;
    }

    private BigDecimal getAmountOfStocks() {
        return mini ? BigDecimal.ONE : BigDecimal.valueOf(100);
    }

    public Option getOption() {
        return option;
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
}
