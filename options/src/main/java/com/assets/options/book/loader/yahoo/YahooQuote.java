package com.assets.options.book.loader.yahoo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class YahooQuote {

    private double regularMarketPrice;
    private String exchangeTimezoneName;
    private long regularMarketTime;

    @JsonCreator
    public YahooQuote(@JsonProperty("regularMarketPrice") double regularMarketPrice,
                      @JsonProperty("exchangeTimezoneName") String exchangeTimezoneName,
                      @JsonProperty("regularMarketTime") long regularMarketTime) {
        this.regularMarketPrice = regularMarketPrice;
        this.exchangeTimezoneName = exchangeTimezoneName;
        this.regularMarketTime = regularMarketTime;
    }

    public double getRegularMarketPrice() {
        return regularMarketPrice;
    }

    public String getExchangeTimezoneName() {
        return exchangeTimezoneName;
    }

    public long getRegularMarketTime() {
        return regularMarketTime;
    }
}
