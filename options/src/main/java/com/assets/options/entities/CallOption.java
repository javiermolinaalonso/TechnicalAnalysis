package com.assets.options.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CallOption extends Option {

    @JsonCreator
    CallOption(@JsonProperty("ticker") String ticker,
               @JsonProperty("currentPrice") BigDecimal currentPrice,
               @JsonProperty("strikePrice") BigDecimal strikePrice,
               @JsonProperty("bid") BigDecimal bid,
               @JsonProperty("ask") BigDecimal ask,
               @JsonProperty("currentDate") LocalDate currentDate,
               @JsonProperty("expirationDate") LocalDate expirationDate,
               @JsonProperty("impliedVolatility") Double impliedVolatility,
               @JsonProperty("riskFree") double riskFree) {
        super(ticker, currentPrice, strikePrice, bid, ask, currentDate, expirationDate, impliedVolatility, riskFree);
    }

    @Override
    public boolean isCall() {
        return true;
    }
}
