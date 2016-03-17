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

    public BigDecimal getCurrentValue(BigDecimal value, LocalDate when, double volatility) {
        Option newOption;
        if(option.isCall()) {
            newOption = new CallOption(value, option.getStrikePrice(), when, option.getExpirationDate(), volatility, option.getRiskFree());
        } else {
            newOption = new PutOption(value, option.getStrikePrice(), when, option.getExpirationDate(), volatility, option.getRiskFree());
        }
        return newOption.getPremium();
    }

    public BigDecimal getTradeAmount() {
        return tradePrice.multiply(BigDecimal.valueOf(contracts));
    }
    public Option getOption() {
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
}
