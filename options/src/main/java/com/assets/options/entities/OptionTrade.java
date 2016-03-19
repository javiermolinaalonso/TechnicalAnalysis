package com.assets.options.entities;

import java.math.BigDecimal;
import java.time.LocalDate;

public class OptionTrade {

    private final Option option;
    private final BigDecimal tradePrice;
    private final int contracts;
    private final String ticker;

    public OptionTrade(Option option, BigDecimal tradePrice, int contracts, String ticker) {
        this.option = option;
        this.tradePrice = tradePrice;
        this.contracts = contracts;
        this.ticker = ticker;
    }

    public BigDecimal getTradeAmount() {
        return tradePrice.multiply(BigDecimal.valueOf(contracts)).multiply(BigDecimal.valueOf(-100));
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
        BigDecimal expectedValue = getExpectedValue(value, option.getExpirationDate()).getPremium().multiply(BigDecimal.valueOf(contracts));
        BigDecimal expirationValue;

        if(option.isCall()) {
            if(isShort()) {
                if(value.compareTo(option.getStrikePrice()) <= 0) {
                    expirationValue = getTradeAmount();
                } else {
                    expirationValue = value.subtract(option.getStrikePrice()).multiply(BigDecimal.valueOf(contracts*100)).add(getTradeAmount());
                }
            } else {
                expirationValue =  expectedValue.multiply(BigDecimal.valueOf(contracts*100)).add(getTradeAmount());
            }
        } else {
            if(isShort()) {
                if(value.compareTo(option.getStrikePrice()) <= 0) {
                    expirationValue = value.subtract(option.getStrikePrice()).multiply(BigDecimal.valueOf(Math.abs(contracts*100))).add(getTradeAmount());
                } else {
                    expirationValue = getTradeAmount();
                }
            } else {
                expirationValue =  expectedValue.multiply(BigDecimal.valueOf(contracts*100)).add(getTradeAmount());
            }
        }
        return expirationValue;
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
        if(option.isCall()) {
            newOption = new CallOption(value, option.getStrikePrice(), when, option.getExpirationDate(), volatility, option.getRiskFree());
        } else {
            newOption = new PutOption(value, option.getStrikePrice(), when, option.getExpirationDate(), volatility, option.getRiskFree());
        }
        return newOption;
    }
}
