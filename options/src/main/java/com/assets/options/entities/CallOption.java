package com.assets.options.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CallOption extends Option {

    CallOption(String ticker, BigDecimal currentPrice, BigDecimal strikePrice, LocalDate currentDate, LocalDate expirationDate, double volatility, double riskFree) {
        super(ticker, currentPrice, strikePrice, currentDate, expirationDate, volatility, riskFree);
    }

    @JsonCreator
    CallOption(@JsonProperty("ticker") String ticker,
               @JsonProperty("currentPrice") BigDecimal currentPrice,
               @JsonProperty("strikePrice") BigDecimal strikePrice,
               @JsonProperty("bid") BigDecimal bid,
               @JsonProperty("ask") BigDecimal ask,
               @JsonProperty("currentDate") LocalDate currentDate,
               @JsonProperty("expirationDate") LocalDate expirationDate,
               @JsonProperty("riskFree") double riskFree) {
        super(ticker, currentPrice, strikePrice, bid, ask, currentDate, expirationDate, riskFree);
    }

    @Override
    public boolean isCall() {
        return true;
    }
}
