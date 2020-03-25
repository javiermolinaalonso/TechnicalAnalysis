package com.assets.options.book.loader.yahoo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class YahooOptionPut extends YahooOption {

    @JsonCreator
    public YahooOptionPut(@JsonProperty("contractSymbol") String contractSymbol,
                          @JsonProperty("strike") double strike,
                          @JsonProperty("bid") double bid,
                          @JsonProperty("ask") double ask,
                          @JsonProperty("currency") String currency,
                          @JsonProperty("lastPrice") double lastPrice,
                          @JsonProperty("contractSize") String contractSize,
                          @JsonProperty("expiration") long expiration) {
        super(contractSymbol, strike, bid, ask, currency, lastPrice, contractSize, expiration);
    }

    @Override
    public boolean isCall() {
        return false;
    }
}
