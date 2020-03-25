package com.assets.options.book.loader.yahoo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class YahooOptionDate {
    private long expirationDate;
    private List<YahooOptionCall> calls;
    private List<YahooOptionPut> puts;

    @JsonCreator
    public YahooOptionDate(@JsonProperty("expirationDate") long expirationDate,
                           @JsonProperty("calls") List<YahooOptionCall> calls,
                           @JsonProperty("puts") List<YahooOptionPut> puts) {
        this.expirationDate = expirationDate;
        this.calls = calls;
        this.puts = puts;
    }

    public long getExpirationDate() {
        return expirationDate;
    }

    public List<YahooOptionCall> getCalls() {
        return calls;
    }

    public List<YahooOptionPut> getPuts() {
        return puts;
    }
}
