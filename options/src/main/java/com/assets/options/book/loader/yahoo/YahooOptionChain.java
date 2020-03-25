package com.assets.options.book.loader.yahoo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

public class YahooOptionChain {

    private List<YahooOptionResult> result;
    private String error;

    @JsonCreator
    public YahooOptionChain(@JsonProperty("result") List<YahooOptionResult> result,
                            @JsonProperty("error") String error) {
        this.result = result;
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public String getZoneId() {
        return result.get(0).getQuote().getExchangeTimezoneName();
    }

    public LocalDate getCurrentDate() {
        String exchangeTimezoneName = result.get(0).getQuote().getExchangeTimezoneName();
        long now = result.get(0).getQuote().getRegularMarketTime();
        return Instant.ofEpochSecond(now).atZone(ZoneId.of(exchangeTimezoneName)).toLocalDate();
    }

    public List<YahooOption> getOptions() {
        return result.get(0).getOptions();
    }

    public String getTicker() {
        return result.get(0).getUnderlyingSymbol();
    }

    public double getCurrentPrice() {
        return result.get(0).getQuote().getRegularMarketPrice();
    }

    @JsonIgnore
    public List<Long> getExpirationDates() {
        return result.get(0).getExpirationDates();
    }
}
