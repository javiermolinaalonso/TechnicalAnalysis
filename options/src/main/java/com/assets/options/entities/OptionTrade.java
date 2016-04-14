package com.assets.options.entities;

import java.math.BigDecimal;
import java.time.LocalDate;

public class OptionTrade {

    private final Option option;
    private final BigDecimal tradePrice;
    private final int contracts;
    private final String ticker;
    private final BigDecimal cost;
    private final boolean mini;

    public OptionTrade(Option option, int contracts, String ticker, BigDecimal contractComission, boolean mini) {
        this.option = option;
        this.tradePrice = option.getPremium();
        this.contracts = contracts;
        this.ticker = ticker;
        this.mini = mini;
        this.cost = BigDecimal.valueOf(contracts).multiply(tradePrice).multiply(getAmountOfStocks())
                .add(contractComission.multiply(BigDecimal.valueOf(Math.abs(contracts))));
    }

    public BigDecimal getCost() {
        return cost;
    }

    public Option getExpectedValue() {
        return option;
    }

    public BigDecimal getTradePrice() {
        return tradePrice;
    }

    public int getContracts() {
        return contracts;
    }

    public String getTicker() {
        return ticker;
    }

    public BigDecimal getExpirationValue(BigDecimal value) {
        BigDecimal expirationValue;

        if (!isShort()) {
            expirationValue = getExpectedValue(value, option.getExpirationDate()).getPremium();
        } else {
            expirationValue = BigDecimal.ZERO;
            if (option.isCall()) {
                if (value.compareTo(option.getStrikePrice()) > 0) {
                    expirationValue = value.subtract(option.getStrikePrice());
                }
            } else {
                if (value.compareTo(option.getStrikePrice()) <= 0) {
                    expirationValue = option.getStrikePrice().subtract(value);
                }
            }
        }
        return expirationValue.multiply(getAmountOfStocks()).multiply(BigDecimal.valueOf(getContracts())).subtract(getCost());
    }

    private boolean isShort() {
        return contracts < 0;
    }

    public Option getExpectedValue(BigDecimal value, LocalDate when) {
        return getExpectedValue(value, when, option.getVolatility());
    }

    public Option getExpectedValue(BigDecimal value) {
        return getExpectedValue(value, option.getCurrentDate(), option.getVolatility());
    }

    public Option getExpectedValue(BigDecimal value, LocalDate when, double volatility) {
        Option newOption;
        if (option.isCall()) {
            newOption = new CallOption(value, option.getStrikePrice(), when, option.getExpirationDate(), volatility, option.getRiskFree());
        } else {
            newOption = new PutOption(value, option.getStrikePrice(), when, option.getExpirationDate(), volatility, option.getRiskFree());
        }
        return newOption;
    }

    private BigDecimal getAmountOfStocks() {
        return mini ? BigDecimal.ONE : BigDecimal.valueOf(100);
    }
}