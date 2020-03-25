package com.assets.options.book.loader.yahoo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.List;

public class YahooResponse {

    private YahooOptionChain optionChain;

    @JsonCreator
    public YahooResponse(@JsonProperty("optionChain") YahooOptionChain optionChain) {
        this.optionChain = optionChain;
    }

    public String getTicker() {
        return optionChain.getTicker();
    }

    public double getCurrentPrice() {
        return optionChain.getCurrentPrice();
    }

    public LocalDate getCurrentDate() {
        return optionChain.getCurrentDate();
    }

    public List<YahooOption> getOptionChain() {
        return optionChain.getOptions();
    }

    @JsonIgnore
    public List<Long> getExpirationDates() {
        return optionChain.getExpirationDates();
    }
}
