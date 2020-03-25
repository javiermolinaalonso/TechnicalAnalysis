package com.assets.options.book.loader.yahoo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class YahooOptionResult {

    private String underlyingSymbol;
    private YahooQuote quote;
    private List<Long> expirationDates;
    private List<YahooOptionDate> optionsDate;

    @JsonCreator
    public YahooOptionResult(@JsonProperty("underlyingSymbol") String underlyingSymbol,
                             @JsonProperty("quote") YahooQuote quote,
                             @JsonProperty("expirationDates") List<Long> expirationDates,
                             @JsonProperty("options") List<YahooOptionDate> optionsDate) {
        this.underlyingSymbol = underlyingSymbol;
        this.quote = quote;
        this.optionsDate = optionsDate;
        this.expirationDates = expirationDates;
    }

    public String getUnderlyingSymbol() {
        return underlyingSymbol;
    }

    public YahooQuote getQuote() {
        return quote;
    }

    public List<Long> getExpirationDates() {
        return expirationDates;
    }

    @JsonIgnore
    public List<YahooOption> getOptions() {
        return Stream.concat(optionsDate.get(0).getCalls().stream(), optionsDate.get(0).getPuts().stream()).collect(Collectors.toList());
    }
}
