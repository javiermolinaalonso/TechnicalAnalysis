package com.assets.options.book.loader.yahoo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class YahooOption {

    protected String contractSymbol;
    protected double strike;
    protected double bid;
    protected double ask;
    protected String currency;
    protected double lastPrice;
    protected String contractSize;
    protected long expiration;
    protected double impliedVolatility;

    @JsonCreator
    public YahooOption(@JsonProperty("contractSymbol") String contractSymbol,
                       @JsonProperty("strike") double strike,
                       @JsonProperty("bid") double bid,
                       @JsonProperty("ask") double ask,
                       @JsonProperty("currency") String currency,
                       @JsonProperty("lastPrice") double lastPrice,
                       @JsonProperty("contractSize") String contractSize,
                       @JsonProperty("expiration") long expiration,
                       @JsonProperty("impliedVolatility") double impliedVolatility) {
        this.contractSymbol = contractSymbol;
        this.strike = strike;
        this.bid = bid;
        this.ask = ask;
        this.currency = currency;
        this.lastPrice = lastPrice;
        this.contractSize = contractSize;
        this.expiration = expiration;
        this.impliedVolatility = impliedVolatility;
    }

    public double getStrike() {
        return strike;
    }

    public double getBid() {
        return bid;
    }

    public double getAsk() {
        return ask;
    }

    public long getExpiry() {
        return expiration;
    }

    public abstract boolean isCall();

    public double getImpliedVolatility() {
        return impliedVolatility;
    }
}
